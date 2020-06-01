let ajaxUserMeals = "ajax/user/meals/";

// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: ajaxUserMeals,
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            })
        }
    );
});

function _clear() {
    document.getElementById('startDate').value = "";
    document.getElementById('endDate').value = "";
    document.getElementById('startTime').value = "";
    document.getElementById('endTime').value = "";
    getExpectedMeals();
}

function filtering() {
let form = document.forms.namedItem('filter');
form.addEventListener('submit', (e)=> {
    e.preventDefault();
    getExpectedMeals();
})
}

function getExpectedMeals() {
    $.ajax({
        url: ajaxUserMeals + "filter",
        type: "GET",
        data: $("#filter").serialize()
    }).done(function (data) {
        updateFilteredTable(data);
    })
}