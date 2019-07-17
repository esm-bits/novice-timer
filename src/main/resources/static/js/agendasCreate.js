$(function() {
    $("#for-back").on("click", function() {
        if(confirm('戻ります。よろしいですか？')){
            location.href = "/agendas";
        }else{
            return false;
        }
    });
    
    $("#for-create").on("click", function() {
        if(confirm('登録します。よろしいですか？')){
            $("form").attr("action", "/agendas/create");
            $("form").submit();
        }else{
            return false;
        }
    });
    
    $("#add-td").on("click", function() {
        var length = $("#create-table > tbody").children().length;
        $('#create-table').append('<tr>' +
            '<td><input type="text" class="form-control input-normal" name="subjectForms[' + length + '].title" /></td>' +
            '<td><input type="text" class="form-control input-normal" name="subjectForms[' + length + '].idobataUser" /></td>' +
            '<td><input type="text" class="form-control input-normal" name="subjectForms[' + length + '].minutes" /></td>' +
            '</tr>');
    });
});
