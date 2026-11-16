package com.example.blogdb.service;

import com.example.blogdb.ContentModule;
import com.example.blogdb.ContentModuleType;
import com.example.blogdb.Post;
import com.example.blogdb.dto.ContentModuleDTO;
import com.example.blogdb.repository.ContentModuleRepository;
import com.example.blogdb.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ContentModuleService {
    private final ContentModuleRepository contentModuleRepository;
    private final PostRepository postRepository;
    private final ContentImageStorageService contentImageStorageService;

    public ContentModuleService(ContentModuleRepository contentModuleRepository,
                                PostRepository postRepository,
                                ContentImageStorageService contentImageStorageService) {
        this.contentModuleRepository = contentModuleRepository;
        this.postRepository = postRepository;
        this.contentImageStorageService = contentImageStorageService;
    }

    @Transactional(readOnly = true)
    public List<ContentModuleDTO> getContentModules(Long postId) {
        List<ContentModule> modules = orderedModules(findPost(postId));
        List<ContentModuleDTO> result = new ArrayList<>();
        for (int i = 0; i < modules.size(); i++) {
            ContentModule module = modules.get(i);
            int order = module.getDisplayOrder() == null ? i + 1 : module.getDisplayOrder();
            result.add(new ContentModuleDTO(module.getId(), module.getType(), module.getText(), module.getLocation(), order));
        }
        return result;
    }

    public void createTextModule(Long postId, String text) {
        Post post = findPost(postId);
        ContentModule module = new ContentModule();
        module.setType(ContentModuleType.TEXT);
        module.setText(text);
        module.setDisplayOrder(nextDisplayOrder(post));
        post.addContentModule(module);
        contentModuleRepository.save(module);
    }

    public void createImageModule(Long postId, MultipartFile file) {
        Post post = findPost(postId);
        ContentModule module = new ContentModule();
        module.setType(ContentModuleType.IMAGE);
        module.setLocation(contentImageStorageService.store(file));
        module.setDisplayOrder(nextDisplayOrder(post));
        post.addContentModule(module);
        contentModuleRepository.save(module);
    }

    public void moveContentModule(Long postId, Long moduleId, int direction) {
        List<ContentModule> modules = orderedModules(findPost(postId));
        for (int i = 0; i < modules.size(); i++) {
            ContentModule module = modules.get(i);
            if (module.getDisplayOrder() == null) {
                module.setDisplayOrder(i + 1);
            }
        }
        int index = -1;
        for (int i = 0; i < modules.size(); i++) {
            if (modules.get(i).getId().equals(moduleId)) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            throw new IllegalArgumentException("콘텐츠 블록을 찾을 수 없습니다.");
        }

        int swapIndex = index + direction;
        if (swapIndex < 0 || swapIndex >= modules.size()) {
            return;
        }

        ContentModule current = modules.get(index);
        ContentModule neighbor = modules.get(swapIndex);
        Integer currentOrder = displayOrderOf(current, index);
        Integer neighborOrder = displayOrderOf(neighbor, swapIndex);
        current.setDisplayOrder(neighborOrder);
        neighbor.setDisplayOrder(currentOrder);
    }

    private List<ContentModule> orderedModules(Post post) {
        return post.getContentModules().stream()
                .sorted(Comparator
                        .comparing((ContentModule module) -> module.getDisplayOrder() == null ? Integer.MAX_VALUE : module.getDisplayOrder())
                        .thenComparing(ContentModule::getId))
                .toList();
    }

    private int nextDisplayOrder(Post post) {
        List<ContentModule> modules = orderedModules(post);
        int maxOrder = modules.stream()
                .map(ContentModule::getDisplayOrder)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0);
        return Math.max(maxOrder, modules.size()) + 1;
    }

    private int displayOrderOf(ContentModule module, int index) {
        return module.getDisplayOrder() == null ? index + 1 : module.getDisplayOrder();
    }

    private Post findPost(Long postId) {
        return postRepository.findWithContentModulesById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }
}
