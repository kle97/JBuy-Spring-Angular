package io.spring.jbuy.features.keyword;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.net.URI;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/admin/keywords")
@Tag(name = "keyword-admin", description = "keyword API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class KeywordAdminController {

    private final KeywordService keywordService;

    @GetMapping("/{keywordId}")
    @Operation(summary = "Find keyword by id", tags = "keyword-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<KeywordResponse> getKeyword(@PathVariable UUID keywordId) {
        return ResponseEntity.ok().body(keywordService.getKeywordResponseById(keywordId));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of keyword as pages", tags = "keyword-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<Page<KeywordResponse>> getKeywordPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(keywordService.getKeywordResponsePageable(pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new keyword", tags = "keyword-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<KeywordResponse> createKeyword(@RequestBody
                                                         @Validated({ValidationGroup.onCreate.class, Default.class})
                                                         @JsonView(BaseView.Create.class)
                                                                 KeywordRequest keywordRequest) {
        KeywordResponse response = keywordService.createKeyword(keywordRequest);
        String keywordId = String.valueOf(response.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{keywordId}")
                .buildAndExpand(keywordId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{keywordId}")
    @Operation(summary = "Update a keyword by id", tags = "keyword-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<KeywordResponse> updateKeyword(@PathVariable UUID keywordId,
                                                         @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                                 KeywordRequest keywordRequest) {
        KeywordResponse response = keywordService.updateKeyword(keywordId, keywordRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{keywordId}")
    @Operation(summary = "Delete a keyword by id", tags = "keyword-admin")
    public ResponseEntity<Void> deleteKeyword(@PathVariable UUID keywordId) {
        keywordService.deleteById(keywordId);
        return ResponseEntity.noContent().build();
    }
}
