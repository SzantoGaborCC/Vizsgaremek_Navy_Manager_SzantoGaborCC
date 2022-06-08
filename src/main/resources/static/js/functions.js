function showDialog(dialogId, url, method, data) {
    $( '#' + dialogId ).dialog({
        dialogClass: "no-close",
        resizable: false,
        height: "auto",
        width: 400,
        modal: true,
        buttons: {
            "Ok": function() {
                $.ajax({
                    method: method,
                    url: url,
                    data: data //{ name: "John", location: "Boston" }

                });
                //$( this ).dialog( "close" );
                location. reload();
            },
            "Cancel": function() {
                $( this ).dialog( "close" );
            }
        }
    });
}

function ajaxFormSubmit(e, formId) {
    e.preventDefault();
    const form = $(this);
    const actionUrl = form.attr('action');
    console.log(actionUrl);
    const formData = form.serialize();
    let data = {};
    $("#" + formId).serializeArray().map(function(x){data[x.name] = x.value;});
    console.log(data['add']);
    const method = data['add'] === "true" ? "POST" : "PUT";
    console.log("method: " + method);

    $.ajax({
        method: method,
        url: actionUrl,
        data: data,
        success: function(data)
        {
            window.location = document.referrer;
        }
    });
}