/**
 * Author: Kathrin Heim 
 */
$(document).ready(function () {

    var search_item = [];

    /**
     * Ajax call:
     * gets the JSON data from availability service and builds the website.
     * Before send: a loading image is displayed while waiting for success.
     * On success: all items are looped through. For each gadget, the title, image id, 
     * availability, total, id, and note are stored in variables. 
     * The title and note are stored in array search_item for the filter function.
     * If there are available items per gadget, a green tile is created, otherwise a red tile. 
     * If none are available, the available counter is also displayed in red. 
     * The html for each tile is created and displayed in div #items (either green_tile or red_tile). 
     * The function init_imgclick is called. 
     */
    $.ajax({
        //url: "functions/availabilityService.php",
        url: "/gadgets/all",
        method: "GET",
        // beforeSend: function () {
        //     $('#loading').append('<img src="assets/img/load.png" alt="loading"> Wird geladen...');
        // },
        success: function (data) {
            $('#loading').css('display', 'none');
            var items = data; //JSON.parse(data);
            //var max = Object.keys(items).length;

            var msg = '';
            var title;
            var note;
            search_item = [];

            for (var i = 0; i < items.length; i++) {
                title = items[i].callno;
                var img_id = items[i].img_id;
                var available = items[i].available;
                var total = items[i].total;
                note = items[i].note;
                search_item.push('' + title.toLowerCase() + ', ' + note.toLowerCase()); //set to lowercase
                var id = items[i].volume;

                if (available !== 0) {//gadget is available 
                    msg += '<div class="green_tile tile ' + i + '" id="' + id + '">';
                } else {//gadget is not available
                    msg += '<div class="red_tile tile ' + i + '" id="' + id + '">';
                }

                msg += '<p class = "title">' + title + '</p>' +
                    '<div class ="photo"><img class="link-image" src="assets/img/' + img_id +
                    '" alt="' + title + '<br>' + note + '" width="220" onerror="this.src=\'assets/img/unavailable.png\';"></div>' +

                    '<p class = "availability"> ';

                if (available !== 0) {
                    msg += available;
                } else {
                    msg += '<span class="allgone">' + available + ' </span>';
                }
                msg += ' von ' + total + '</p></div>';
            }
            $('#items').html(msg);
            init_imgclick();
        }
    });

    /**
     * Function init_imgclick gets the alt-text of the image (= title & note for the gadget) and
     * calls function loadtoast().
     * 
     */
    var init_imgclick = function () {
        $('.link-image').click(function () {
            var note = $(this).attr("alt");
            loadtoast(note);
        });
    };

    /**
     * Function filter stores the input in the search-box in a variable and checks the array search_items.
     * If there is a match, the corresponding tile is displayed.
     */
    var filter = function () {
        var input = $('#filter-search').val().toLowerCase(); //input text to lowercase
        $('.tile').css('display', 'none');
        //loop through array
        for (var index in search_item) {
            if (search_item[index].indexOf(input) > -1) {
                $('.' + index + '').css('display', '');
            }
        }
    };

    $('#filter-search').on('input', filter);
});
