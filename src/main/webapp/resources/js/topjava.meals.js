// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/user/meals/",
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
}