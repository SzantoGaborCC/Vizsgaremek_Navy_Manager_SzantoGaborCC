function showDialog(dialogSelector, title,  url, method, data) {
    $(dialogSelector ).dialog({
        dialogClass: "no-close",
        title : title,
        resizable: false,
        height: "auto",
        width: 400,
        modal: true,
        buttons: {
            "Ok": function() {
                let that = this;
                $.ajax({
                    method: method,
                    url: url,
                    data: data,
                    success: function(resp) {
                        setTimeout(function(){
                            window.location.reload();
                        },1);
                    },
                    error: function (response) {
                        $(that).dialog("close");
                        if (typeof response.responseJSON.errorDescription !== 'undefined' &&
                            response.responseJSON.errorDescription !== null) {
                            $("h2").after('<span class="validationError">' + response.responseJSON.errorDescription + '</span>');
                        }
                    }

                });

            },
            "Cancel": function() {
                $( this ).dialog( "close" );
            }
        }
    });
}

function ajaxFormSubmit(e, formSelector) {
    e.preventDefault();
    let data = {};
    $(formSelector).serializeArray().map(function(x){data[x.name] = x.value;});
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
        data: JSON.stringify(data),
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

function fillSelectWithOptionsAccordingToFilterer(selectToBeFilled, filterer, originalValues, originallySelected, prependWithUnassigned) {
    const filterText = $(filterer +  " :selected").html();
    if (prependWithUnassigned === 'prependWithUnassigned') {
        $(selectToBeFilled).prepend($('<option></option>').val(-1).html('--------------Unassigned--------------'));
    }
    let found = false;
    $.each(originalValues, function (key, value) {
        if (value.includes(filterText)) {
            if (key.includes(originallySelected)) {
                found = true;
                $(selectToBeFilled).append($('<option selected="selected"></option>').val(key).html(value));
            } else {
                $(selectToBeFilled).append($('<option></option>').val(key).html(value));
            }
        }
    });
    if (found === false) {
        $(selectToBeFilled).val($(selectToBeFilled + " option:first").val());
    }
}

function saveOriginalOptions(selectToBeSaved) {
    let saved = {};
    $(selectToBeSaved +  " option").each(function()
    {
        saved[$(this).val()] = $(this).html();
    });
    return saved;
}

function sortByColumn(that) {
    let table = that.closest("table");
    this.asc = !this.asc;
    let rows = table.find('tr:gt(0)').toArray().sort(comparer(that.index(), !this.asc))
    for (let i = 0; i < rows.length; i++) {
        table.append(rows[i])
    }
}

function comparer(index, desc) {
    return function(a, b) {
        let valA = getCellValue(a, index),
            valB = getCellValue(b, index)
        let result = $.isNumeric(valA) && $.isNumeric(valB) ? valA - valB : valA.toString().localeCompare(valB);
        if (desc) {
            result *= -1;
        }
        return result;
    }
}

function getCellValue(row, index) {
    return $(row).children('td').eq(index).text()
}