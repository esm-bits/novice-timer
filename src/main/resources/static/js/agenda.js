$(function() {
    $("#for-back").on("click", function() {
        location.href = "/agendas";
    });
    
    $(".for-start").on("click", function() {
        startTimer($(this).val());
    });
    
    $("#for-stop").on("click", function() {
        stopTimer();
    });
});

function startTimer(number) {
    var agendaId = $("#agenda-id").text();
    var body = { "state": "start" }
    
    var message = '#message-for-start-' + number;
    $(message).text('');
    
    $.ajax({
        type: "PUT",
        url : "/api/agendas/" + agendaId + "/subjects/" + number + "/timers",
        data: JSON.stringify(body),
        contentType: 'application/json',
        success  : function() {
        },
        error    : function() {
            $(message).text('error!');
        }
    });
}

function stopTimer() {
	$('#message-for-stop').text('');
    
    $.ajax({
        type: "DELETE",
        url : "/api/timers",
        success  : function() {
        },
        error    : function() {
            $('#message-for-stop').text('error!');
        }
    });
}
