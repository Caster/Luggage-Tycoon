// The API will call this function when the page has finished downloading the JavaScript for the player API
function onYouTubePlayerAPIReady() {

    $(document).ready(function() {
        $(".fancybox").fancybox({
            afterShow: function() {
                var $element = $(this.element);
                
                if (typeof($element.attr('data-youtube-url')) !== 'undefined') {
                    var playerId = 'youtube-player-frame',
                    $playerFrame = $.fancybox.inner.wrapInner('<div id="' + playerId + '" />');
                    
                    // Create video player object and add event listeners
                    console.log($.fancybox.inner.innerWidth(), $.fancybox.inner.innerHeight());
                    var playbackQuality = 'hd1080',
                        player = new YT.Player(playerId, {
                            events: {
                                'onReady': function(event) {
                                    $.fancybox.inner.find('iframe')
                                    .removeAttr('width')
                                    .removeAttr('height')
                                    .css({
                                        width: '100%',
                                        height: '100%'
                                    });
                                    event.target.setPlaybackQuality(playbackQuality);
                                    event.target.playVideo();
                                },
                                'onStateChange': function(event) {
                                    if (event.data === YT.PlayerState.BUFFERING) {
                                        // http://stackoverflow.com/a/10757854/962603
                                        event.target.setPlaybackQuality(playbackQuality);
                                    }
                                }
                            },
                            videoId: $element.attr('data-youtube-url'),
                                                width: $.fancybox.inner.innerWidth(),
                                                height: $.fancybox.inner.innerHeight()
                        });
                }
            },
            beforeShow: function() {
                
                this.title = $(this.element).find('img').attr('alt');
            },
            helpers: {
                overlay: {
                    css: {
                        'background' : 'rgba(225, 205, 174, 0.95)'
                    }
                }
            },
            padding: 0
        });
    });

}