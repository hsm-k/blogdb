/**
 * 게시글 작성 시 텍스트/이미지 콘텐츠 블록을 동적으로 추가·삭제하는 스크립트
 */
(function () {
    const container = document.getElementById('moduleContainer');
    const addTextBtn = document.getElementById('addTextBlock');
    const addImageBtn = document.getElementById('addImageBlock');

    if (!container || !addTextBtn || !addImageBtn) return;

    let blockCount = 0;

    /**
     * 텍스트 블록 추가
     */
    addTextBtn.addEventListener('click', function () {
        blockCount++;
        const block = document.createElement('div');
        block.className = 'module-block';
        block.innerHTML =
            '<div class="module-block-header">' +
                '<span class="module-block-label">📝 텍스트 블록 #' + blockCount + '</span>' +
                '<button type="button" class="module-remove-btn" title="이 블록 삭제">✕</button>' +
            '</div>' +
            '<input type="hidden" name="moduleTypes" value="TEXT">' +
            '<textarea name="moduleTexts" placeholder="이 블록에 들어갈 텍스트를 입력하세요." class="module-textarea"></textarea>';

        container.appendChild(block);
        attachRemoveHandler(block);
        block.querySelector('textarea').focus();
    });

    /**
     * 이미지 블록 추가
     */
    addImageBtn.addEventListener('click', function () {
        blockCount++;
        var inputId = 'moduleFileInput_' + blockCount;

        var block = document.createElement('div');
        block.className = 'module-block';
        block.innerHTML =
            '<div class="module-block-header">' +
                '<span class="module-block-label">🖼 이미지 블록 #' + blockCount + '</span>' +
                '<button type="button" class="module-remove-btn" title="이 블록 삭제">✕</button>' +
            '</div>' +
            '<input type="hidden" name="moduleTypes" value="IMAGE">' +
            '<!-- 텍스트 placeholder 를 빈 값으로 보냄 -->' +
            '<input type="hidden" name="moduleTexts" value="">' +
            '<label class="file-drop-zone" for="' + inputId + '">' +
                '<span class="upload-symbol">↑</span>' +
                '<strong>이미지 파일 선택</strong>' +
                '<small>JPG, PNG, GIF 등 이미지 파일을 선택하세요.</small>' +
                '<input id="' + inputId + '" name="moduleFiles" type="file" accept="image/*">' +
            '</label>' +
            '<img class="image-preview module-img-preview" alt="미리보기" hidden>';

        container.appendChild(block);
        attachRemoveHandler(block);
        attachImagePreview(block, inputId);
    });

    /**
     * 삭제 버튼 핸들러
     */
    function attachRemoveHandler(block) {
        var btn = block.querySelector('.module-remove-btn');
        btn.addEventListener('click', function () {
            block.classList.add('module-block-removing');
            setTimeout(function () {
                block.remove();
            }, 200);
        });
    }

    /**
     * 이미지 미리보기 핸들러
     */
    function attachImagePreview(block, inputId) {
        var input = document.getElementById(inputId);
        var preview = block.querySelector('.module-img-preview');
        input.addEventListener('change', function () {
            if (input.files && input.files[0]) {
                preview.src = URL.createObjectURL(input.files[0]);
                preview.hidden = false;
            } else {
                preview.hidden = true;
                preview.removeAttribute('src');
            }
        });
    }
})();
