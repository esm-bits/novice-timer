const Timer = function(index) {
    this.isRunning = false;
    this.hasPlayedHalf = false;
    this.timeMillisecond = parseInt($("#minutes-" + index).text(), 10) * 60 * 1000;
    this.halfTimeMillisecond = this.timeMillisecond / 2;
    this.remainingTimeMillisecond = this.timeMillisecond;

    this.radiosElement = $('input[name=radios]');
    this.timerElement = $('#timer');
    this.startStopButtonElement = $('#for-start-stop');
    this.resetButtonElement = $('#for-reset');
}

Timer.prototype.clickStartStopButton = function() {
    if (this.isRunning) {
        this.stop();
    } else {
        this.start();
    }
}

Timer.prototype.clickResetButton = function() {
    this.reset();
}

Timer.prototype.start = function() {
    this.radiosElement.prop('disabled', true);
    this.resetButtonElement.prop("disabled", true);
    this.startStopButtonElement.text('停止');

    this.isRunning = true;
    const endDate = new Date();
    endDate.setMilliseconds(endDate.getMilliseconds() + this.remainingTimeMillisecond);

    const countdown = () => {
        if (!this.isRunning) {
            return;
        }

        const nowDate = new Date();
        this.remainingTimeMillisecond = endDate - nowDate;

        if (this.remainingTimeMillisecond >= 0) {
            if (!this.hasPlayedHalf && this.halfTimeMillisecond >= this.remainingTimeMillisecond) {
                $('#audio-half')[0].play();
                this.hasPlayedHalf = true;
            }
            this.show();
            setTimeout(countdown, 10);
        } else {
            this.remainingTimeMillisecond = 0;
            this.stop();
            // ノーマルJavaScriptでも使用できるメソッドをjQueryオブジェクトに対して使用しようとする時には、get(0),[0]などが必要になる
            $('#audio-end')[0].play();
        }
    };
    countdown();
}

Timer.prototype.stop = function() {
    this.radiosElement.prop('disabled', false);
    this.resetButtonElement.prop("disabled", false);
    this.startStopButtonElement.text('開始');
    this.show();
    this.isRunning = false;
}

Timer.prototype.reset = function() {
    this.remainingTimeMillisecond = this.timeMillisecond;
    this.show();
    this.hasPlayedHalf = false;
}

Timer.prototype.show = function() {
    const remainingMinute = Math.floor(this.remainingTimeMillisecond / (1000 * 60));
    const remainingSecond = Math.floor((this.remainingTimeMillisecond - remainingMinute * 1000 * 60) / 1000);
    this.timerElement.text(
        (remainingMinute < 10 ? '0' + remainingMinute : remainingMinute)
        + ':' +
        (remainingSecond < 10 ? '0' + remainingSecond : remainingSecond)
    );
}

/** HTMLの読み込みが完了してから実行される */
$(function() {
    $("input[name='radios']:radio").parent("td").on("click", function() {
        if (!$(this).children("input[name='radios']:radio").prop("disabled")) {
            // propで状態を変化しても、changeイベントは発火しないので .change() を呼び出している
            $(this).children("input[name='radios']:radio").prop("checked", true).change();
        }
    });

    const timers = new Array();
    for (let i = 0; i < $("#table tbody").children().length; i++) {
        timers.push(new Timer(i));
    }
    timers[0].show();

    $("input[name='radios']:radio").change( function() {
        timers[$("input:radio[name='radios']:checked").val()].show();
    });

    $("#for-start-stop").on("click", function() {
        timers[$("input:radio[name='radios']:checked").val()].clickStartStopButton();
    });

    $("#for-reset").on("click", function() {
        timers[$("input:radio[name='radios']:checked").val()].clickResetButton();
    });

    $("#timer-range").on("input", function () {
        const size = $(this).val() * 50;
    	$("#timer").css("font-size", size + "px");
    	$("#timer").css("line-height", size + "px");
    });

    $("#for-play-half").on("click", function() {
        $('#audio-half')[0].play();
    });

    $("#for-onoff-half").on("click", function() {
        if ($('#audio-half')[0].volume === 0.0) {
            $('#audio-half')[0].volume = 1.0
            $("#for-onoff-half").attr("src","/image/on.png");
        } else {
            $('#audio-half')[0].volume = 0.0
            $("#for-onoff-half").attr("src","/image/off2.png");
        }
    });

    $("#for-play-end").on("click", function() {
        $('#audio-end')[0].play();
    });

    $("#for-onoff-end").on("click", function() {
        if ($('#audio-end')[0].volume === 0.0) {
            $('#audio-end')[0].volume = 1.0
            $("#for-onoff-end").attr("src","/image/on.png");
        } else {
            $('#audio-end')[0].volume = 0.0
            $("#for-onoff-end").attr("src","/image/off2.png");
        }
    });

    $("#for-back").on("click", function() {
        if (confirm('戻ります。よろしいですか？')){
            location.href = "/agendas";
        } else {
            return false;
        }
    });
});
