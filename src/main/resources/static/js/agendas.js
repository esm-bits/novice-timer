$(function() {
    $("#for-create").on("click", function() {
        location.href = "agendas/create";
    });
    
    $("#for-edit").on("click", function() {
        var value = $("input:radio[name='selectAgenda']:checked").val();
        location.href = "agendas/edit?id=" + value;
    });
    
    $("#for-detail").on("click", function() {
        var value = $("input:radio[name='selectAgenda']:checked").val();
        location.href = "agendas/detail?id=" + value;
    });
    
    $("#for-delete").on("click", function() {
        $("form").attr("action", "/agendas/delete");
        $("form").submit();
    });
});
