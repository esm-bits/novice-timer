$(function() {
    $("#for-back").on("click", function() {
        location.href = "/agendas";
    });
    
    $("#for-edit").on("click", function() {
        if(confirm('登録します。よろしいですか？')){
            $("form").attr("action", "/agendas/edit");
            $("form").submit();
        }else{
            return false;
        }
    });
    
    $("#add-td").on("click", function() {
        var length = $("#edit-table > tbody").children().length;
        $('#edit-table').append('<tr>' +
            '<td><input type="text" class="form-control input-normal" name="subjectForms[' + length + '].title" /></td>' +
            '<td><input type="text" class="form-control input-normal" name="subjectForms[' + length + '].idobataUser" /></td>' +
            '<td><input type="text" class="form-control input-normal" name="subjectForms[' + length + '].minutes" /></td>' +
            '</tr>');
    });
});
