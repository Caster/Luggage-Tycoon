$(document).ready(function() {
    $(".fancybox").fancybox({
        beforeShow: function () {
            this.title = $(this.element).find('img').attr('alt');
        },
        helpers : {
            overlay : {
                css : {
                    'background' : 'rgba(225, 205, 174, 0.95)'
                }
            }
        },
        padding: 0
    });
});