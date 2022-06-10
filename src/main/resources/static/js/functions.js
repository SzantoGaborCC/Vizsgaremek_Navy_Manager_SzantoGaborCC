function showDialog(dialogId, title,  url, method, data) {
    $( '#' + dialogId ).dialog({
        dialogClass: "no-close",
        title : title,
        resizable: false,
        height: "auto",
        width: 400,
        modal: true,
        buttons: {
            "Ok": function() {
                $.ajax({
                    method: method,
                    url: url,
                    data: data, //{ name: "John", location: "Boston" }
                    success: function(resp) {
                        setTimeout(function(){
                            window.location.reload();
                        },1);
                    }
                });

            },
            "Cancel": function() {
                $( this ).dialog( "close" );
            }
        }
    });
}

function ajaxFormSubmit(e, formId) {
    e.preventDefault();
    let data = {};
    $("#" + formId).serializeArray().map(function(x){data[x.name] = x.value;});
    const url = data['url'];
    delete data['url'];
    const method = data['method'];
    delete data['method'];
    const redirectTo = data['redirectTo'];
    delete data['redirectTo'];
    $('span.validationError').remove()
    $.ajax({
        method: method,
        url : url,
        data: data,
        success: function(response)
        {
            if (typeof redirectTo !== 'undefined' )
                window.location = redirectTo;
            else
                window.location = document.referrer;
        },
        error: function(response)
        {
            $.each(response.responseJSON.errorMessages, function (key, value) {
                $('#' + key).after('<span class="validationError">' + value + '</span>');
            });
        }
    });
}