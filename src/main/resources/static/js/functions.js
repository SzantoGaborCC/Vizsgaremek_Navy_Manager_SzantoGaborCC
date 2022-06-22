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
    const method = data['add'] === 'true' ? 'POST' : 'PUT';
    delete data['add'];
    const redirectTo = data['redirectTo'];
    delete data['redirectTo'];
    $('span.validationError').remove()
    $.ajax({
        method: method,
        url : url,
        data: /*data,*/ JSON.stringify(data),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function(response)
        {
            if (typeof redirectTo !== 'undefined' )
                window.location = redirectTo;
            else
                window.location = document.referrer;
        },
        error: function(response)
        {
            if (typeof response.responseJSON.errorDescription !== 'undefined' &&
                response.responseJSON.errorDescription !== null)
                $("[id$='Form']").before('<span class="validationError">' + response.responseJSON.errorDescription + '</span>');
            $.each(response.responseJSON.errorMessages, function (key, value) {
                $('#' + key).after('<span class="validationError">' + value + '</span>');
            });
        }
    });
}

function fillWithOptionsAccordingTo(selectToBeFilled, filterer, originalValues, prependWithUnassigned) {
    const filterText = $(filterer +  " :selected").html();
    $.each(originalValues, function (key, value) {
        if (value.includes(filterText)) {
            $(selectToBeFilled).append($('<option></option>').val(key).html(value));
        }
    });
    if (prependWithUnassigned === 'prependWithUnassigned') {
        $(selectToBeFilled).prepend($('<option></option>').val(-1).html('-------------Unassigned-------------'));
        $(selectToBeFilled).val($(selectToBeFilled + " option:first").val());
    }
}

function saveOriginalOptions(selectToBeSaved) {
    let saved = {};
    console.log('Saving select options:');
    $(selectToBeSaved +  " option").each(function()
    {
        console.log('val: ' + $(this).val() + " html: " + $(this).html());
        saved[$(this).val()] = $(this).html();
    });
    console.log('Saved select options.');
    return saved;
}