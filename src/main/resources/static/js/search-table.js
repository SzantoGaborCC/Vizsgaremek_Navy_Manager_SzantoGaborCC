(function ($) {
    'use strict';
    function setUpOptions(options) {
        const placeholderConst = "Enter a search term";
        var defaultOptions = options || { placeholder: placeholderConst, filterTrClass: "" };
        if (defaultOptions.placeholder === undefined)
            defaultOptions.placeholder = placeholderConst;
        if (defaultOptions.filterTrClass === undefined)
            defaultOptions.filterTrClass = "";
        return defaultOptions;
    }
    var searchTable = function (table, options) {
        this.setUp = function () {
            var defaultOptions = setUpOptions(options);
            var inputSearch = $("<input>", { type: "text", 'class': "form-control", placeholder: defaultOptions.placeholder });
            var spanSearch = $("<span>", { 'class': "add-on" }).append($("<i>", { 'class': "icon icon-search" }))
            var divSearch = $("<div>", { 'class': "row-fluid input-append" });
            divSearch.append(inputSearch).append(spanSearch);
            $(table).before(divSearch);
            var $rows = $(table).find('tbody tr' + defaultOptions.filterTrClass);
            inputSearch.keyup(function () {
                if (typeof defaultOptions.before === 'function')
                    defaultOptions.before();
                var val = '^(?=.*' + $.trim($(this).val()).split(/\s+/).join(')(?=.*') + ').*$',
                    reg = RegExp(val, 'i'),
                    text;
                $rows.show().filter(function () {
                    text = $(this).text().replace(/\s+/g, ' ');
                    return !reg.test(text);
                }).hide();
            });
        }
    }
    $.fn.searchTable = function (options) {
        var st = new searchTable(this, options);
        st.setUp();
    }
})(jQuery);
