package com.example.football.controller;

import com.example.football.entity.CommunityPost;
import com.example.football.entity.Reply;
import com.example.football.entity.Standing;
import com.example.football.entity.User;
import com.example.football.repository.CommunityRepository;
import com.example.football.repository.ReplyRepository;
import com.example.football.service.StandingsService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.http.MediaType;


@Controller
public class CommunityController {

    private final CommunityRepository communityRepository;
    private final ReplyRepository replyRepository;
    private final StandingsService standingsService;

    // C:/Project/Football-Project/uploads/ 내부에 images/, attachments/ 폴더가 생깁니다.
    private final String uploadDir = "C:/Project/Football-Project/uploads/";
    
    // 예: DB에는 /uploads/images/filename.jpg 이렇게 들어가도록 상대 경로를 지정
    private final String relativePath = "/uploads/";

    @Autowired
    public CommunityController(CommunityRepository communityRepository,
                               ReplyRepository replyRepository,
                               StandingsService standingsService) {
        this.communityRepository = communityRepository;
        this.replyRepository = replyRepository;
        this.standingsService = standingsService;
    }

    // 커뮤니티 목록
    @GetMapping("/community")
    public String communityHome(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<CommunityPost> postsPage = communityRepository.findAll(pageable);

        model.addAttribute("posts", postsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postsPage.getTotalPages());

        // standings 부분
        List<Standing> standings = standingsService.getStandings();
        model.addAttribute("standings", standings);

        return "community";
    }

    // 새 글 작성 폼
    @GetMapping("/community/new")
    public String newPostForm(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("author", loggedInUser.getUsername());
        return "newPost";
    }

    // 새 글 등록 (이미지/첨부파일 업로드 처리)
    @PostMapping("/community")
    public String createPost(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             @RequestParam(value = "attachment", required = false) MultipartFile attachment,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        // 로그인된 사용자 체크
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        // 게시글 객체 생성
        CommunityPost post = new CommunityPost();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(loggedInUser.getUsername());

        // 업로드 폴더 생성(없으면) - images, attachments
        ensureUploadDirExists();

        // -----------------------
        // 1) 이미지 업로드 처리
        // -----------------------
        if (image != null && !image.isEmpty()) {
            try {
                // 파일명 중 특수문자 제거(간단 처리)
                String originalFilename = image.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                // 겹치지 않도록 현재 시간 + 원본 파일명
                String fileName = System.currentTimeMillis() + "_" + originalFilename;
                
                // 실제 저장될 절대 경로 (images 폴더)
                String absoluteFilePath = uploadDir + "images/" + fileName;
                
                // 파일 업로드(저장)
                image.transferTo(new File(absoluteFilePath));
                
                // DB에 저장할 때는 웹에서 접근 가능한 상대 경로로...
                String imagePath = relativePath + "images/" + fileName;
                post.setImagePath(imagePath);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("message", "이미지 업로드 중 오류가 발생했습니다.");
                return "redirect:/community";
            }
        }

        // -----------------------
        // 2) 첨부파일 업로드 처리
        // -----------------------
        if (attachment != null && !attachment.isEmpty()) {
            try {
                String originalFilename = attachment.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                String fileName = System.currentTimeMillis() + "_" + originalFilename;
                
                // 실제 저장될 절대 경로 (attachments 폴더)
                String absoluteFilePath = uploadDir + "attachments/" + fileName;
                
                // 파일 업로드(저장)
                attachment.transferTo(new File(absoluteFilePath));
                
                // DB에는 /uploads/attachments/파일명 식으로 저장
                String attachmentPath = relativePath + "attachments/" + fileName;
                post.setAttachmentPath(attachmentPath);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("message", "첨부파일 업로드 중 오류가 발생했습니다.");
                return "redirect:/community";
            }
        }

        // DB 저장
        communityRepository.save(post);
        redirectAttributes.addFlashAttribute("message", "게시글이 작성되었습니다.");
        return "redirect:/community";
    }

    // 업로드 폴더가 없으면 생성
    private void ensureUploadDirExists() {
        File imageDir = new File(uploadDir + "images/");
        File attachmentDir = new File(uploadDir + "attachments/");

        if (!imageDir.exists() && imageDir.mkdirs()) {
            System.out.println("이미지 업로드 디렉토리가 생성되었습니다: " + imageDir.getAbsolutePath());
        }
        if (!attachmentDir.exists() && attachmentDir.mkdirs()) {
            System.out.println("첨부파일 업로드 디렉토리가 생성되었습니다: " + attachmentDir.getAbsolutePath());
        }
    }
    
 // 게시글 상세 보기
 	@GetMapping("/community/{id}")
 	public String viewPost(@PathVariable("id") Long id, Model model) {
 		CommunityPost post = communityRepository.findById(id).orElse(null);
 		if (post != null) {
 			post.setViews(post.getViews() + 1);
 			communityRepository.save(post);
 			model.addAttribute("post", post);

 			List<Reply> replies = replyRepository.findByPostId(id);
 			model.addAttribute("replies", replies);
 		}

 		List<Standing> standings = standingsService.getStandings();
 		model.addAttribute("standings", standings);

 		return "viewPost";
 	}
 	
	// 답글 추가
	@PostMapping("/community/{id}/reply")
	public String addReply(@PathVariable("id") Long id, @RequestParam("content") String content, HttpSession session,
			RedirectAttributes redirectAttributes) {
		String loggedInUsername = (String) session.getAttribute("loggedInUsername");

		if (loggedInUsername == null) {
			redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
			return "redirect:/login";
		}

		CommunityPost post = communityRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

		Reply reply = new Reply();
		reply.setPost(post);
		reply.setAuthor(loggedInUsername);
		reply.setContent(content);

		replyRepository.save(reply);

		return "redirect:/community/" + id;
	}

	// 게시글 수정 폼
	@GetMapping("/community/{id}/edit")
	public String editPostForm(@PathVariable("id") Long id, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		CommunityPost post = communityRepository.findById(id).orElse(null);
		String loggedInUsername = (String) session.getAttribute("loggedInUsername");

		if (post == null) {
			redirectAttributes.addFlashAttribute("message", "게시글을 찾을 수 없습니다.");
			return "redirect:/community";
		}

		if (!post.getAuthor().equals(loggedInUsername)) {
			redirectAttributes.addFlashAttribute("message", "수정 권한이 없습니다.");
			return "redirect:/community";
		}

		model.addAttribute("post", post);
		return "editPost";
	}

	// 게시글 수정 처리
	@PostMapping("/community/{id}/update")
	public String updatePost(@PathVariable("id") Long id, @RequestParam("title") String title,
			@RequestParam("content") String content, HttpSession session, RedirectAttributes redirectAttributes) {
		CommunityPost post = communityRepository.findById(id).orElse(null);
		String loggedInUsername = (String) session.getAttribute("loggedInUsername");

		if (post == null) {
			redirectAttributes.addFlashAttribute("message", "게시글을 찾을 수 없습니다.");
			return "redirect:/community";
		}

		if (!post.getAuthor().equals(loggedInUsername)) {
			redirectAttributes.addFlashAttribute("message", "수정 권한이 없습니다.");
			return "redirect:/community";
		}

		post.setTitle(title);
		post.setContent(content);
		communityRepository.save(post);
		redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 수정되었습니다.");
		return "redirect:/community/" + id;
	}

	// 게시글 삭제
	@PostMapping("/community/{id}/delete")
	public String deletePost(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
		String loggedInUsername = (String) session.getAttribute("loggedInUsername");

		CommunityPost post = communityRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

		if (!post.getAuthor().equals(loggedInUsername)) {
			redirectAttributes.addFlashAttribute("message", "삭제 권한이 없습니다.");
			return "redirect:/community";
		}

		communityRepository.delete(post);
		redirectAttributes.addFlashAttribute("message", "게시글이 삭제되었습니다.");
		return "redirect:/community";
	}

	// 답글 수정 폼
	@GetMapping("/community/{postId}/reply/{replyId}/edit")
	public String editReplyForm(@PathVariable("postId") Long postId, @PathVariable("replyId") Long replyId,
			HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		String loggedInUsername = (String) session.getAttribute("loggedInUsername");

		if (loggedInUsername == null) {
			redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
			return "redirect:/login";
		}

		Reply reply = replyRepository.findById(replyId)
				.orElseThrow(() -> new IllegalArgumentException("답글을 찾을 수 없습니다."));

		if (!reply.getAuthor().equals(loggedInUsername)) {
			redirectAttributes.addFlashAttribute("message", "수정 권한이 없습니다.");
			return "redirect:/community/" + postId;
		}

		model.addAttribute("reply", reply);
		model.addAttribute("postId", postId);
		return "editReply";
	}

	// 답글 수정 처리
	@PostMapping("/community/{postId}/reply/{replyId}/update")
	public String updateReply(@PathVariable("postId") Long postId, @PathVariable("replyId") Long replyId,
			@RequestParam("content") String content, HttpSession session, RedirectAttributes redirectAttributes) {
		String loggedInUsername = (String) session.getAttribute("loggedInUsername");

		if (loggedInUsername == null) {
			redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
			return "redirect:/login";
		}

		Reply reply = replyRepository.findById(replyId)
				.orElseThrow(() -> new IllegalArgumentException("답글을 찾을 수 없습니다."));

		if (!reply.getAuthor().equals(loggedInUsername)) {
			redirectAttributes.addFlashAttribute("message", "수정 권한이 없습니다.");
			return "redirect:/community/" + postId;
		}

		reply.setContent(content);
		replyRepository.save(reply);

		redirectAttributes.addFlashAttribute("message", "답글이 수정되었습니다.");
		return "redirect:/community/" + postId;
	}

	// 답글 삭제
	@PostMapping("/community/{postId}/reply/{replyId}/delete")
	public String deleteReply(@PathVariable("postId") Long postId, @PathVariable("replyId") Long replyId,
			HttpSession session, RedirectAttributes redirectAttributes) {
		String loggedInUsername = (String) session.getAttribute("loggedInUsername");

		if (loggedInUsername == null) {
			redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
			return "redirect:/login";
		}

		Reply reply = replyRepository.findById(replyId)
				.orElseThrow(() -> new IllegalArgumentException("답글을 찾을 수 없습니다."));

		// 작성자 확인
		if (!reply.getAuthor().equals(loggedInUsername)) {
			redirectAttributes.addFlashAttribute("message", "삭제 권한이 없습니다.");
			return "redirect:/community/" + postId;
		}

		replyRepository.delete(reply); // 답글 삭제
		redirectAttributes.addFlashAttribute("message", "답글이 삭제되었습니다.");
		return "redirect:/community/" + postId;
		}

	 
    @GetMapping("/uploads/images/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            // 실제 파일 경로
            String filePath = uploadDir + "images/" + filename;
            Resource file = new UrlResource("file:" + filePath);

            if (file.exists() && file.isReadable()) {
                // 성공적으로 Resource를 찾았다면 그대로 반환
                return ResponseEntity.ok().body(file);
            } else {
                throw new RuntimeException("파일을 찾을 수 없습니다: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("파일 로드 중 오류가 발생: " + filename, e);
        }
    }
    // 첨부파일 다운로드
    @GetMapping("/uploads/attachments/{filename}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable String filename) {
        try {
            String filePath = uploadDir + "attachments/" + filename;
            Resource resource = new UrlResource("file:" + filePath);

            if (resource.exists() && resource.isReadable()) {

                // 1) OS로부터 MIME 타입 추론
                String mimeType = Files.probeContentType(Paths.get(filePath));
                // 2) 못 찾으면 기본값 지정
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

            } else {
                throw new RuntimeException("첨부파일을 찾을 수 없습니다: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("첨부파일 로드 중 오류가 발생: " + filename, e);
        }
    }
   
}