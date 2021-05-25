"use strict";

$(document).ready(function(){

    let currentUserUrl = "http://localhost:8080/api/user";
    let adminUrl = "http://localhost:8080/api/admin";
    let getUserUrl = "http://localhost:8080/api/admin/get/";
    let deleteUserUrl = "http://localhost:8080/api/admin/delete/";
    let updateUserUrl = "http://localhost:8080/api/admin/update/";

    //Fill the header
    $.ajax({
        type:"GET",
        url:currentUserUrl,
        dataType: "json",
        success: function (response) {
            // Filling the header
            let result = "<strong>" + response.email + "</strong>" + " with roles " + getRolesFormat(response.roles);
            $("#navBarHeader").append(result);
        }
    })

    // Fill the user table
    $.ajax({
        type: "GET",
        url: currentUserUrl,
        dataType: "json",
        success: function (response) {
            let result = "<tr>" + lineUser(response) + "</tr>";
            $("#userData").append(result);
        }
    })

    // Fill admin table
    $.ajax({
        type: "GET",
        url: adminUrl,
        dataType: "json",
        success: function (response) {
            let result = "";
            let len = response.length

            for (let i = 0; i < len; i++) {
                result = result + "<tr>" + lineUser(response[i]) + lineButtons(response[i]) + "</tr>"
            }
            $("#tableOfUsers").append(result);
        }
    })

    // Submit new user
    $("#newUser").submit(function () {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "POST",
            url: adminUrl,
            dataType: 'json',
            data: JSON.stringify($(this).serializeObject())
           })
    })

    // Update
    $(document).on('click', '.upd', function () {
        let id = parseInt($(this).attr('id').substr(7));
        $("#updateModal").modal('show');

        $.ajax({
            url: getUserUrl + id,
            type: "GET",
            dataType: "json",
            success: function (response) {
                    $("#id_edit").val(response.id);
                    $("#fn_edit").val(response.firstName);
                    $("#ln_edit").val(response.lastName);
                    $("#age_edit").val(response.age);
                    $("#email_edit").val(response.email);
                    $("#password_edit").val(response.password);
                    $("#roles_edit").empty().append(
                        "<option "
                        + (response.roles.filter(x => x.role === "ROLE_ADMIN").length === 1 ? "selected" : "")
                        + "> ADMIN </option> " +
                        "<option "
                        + (response.roles.filter(x => x.role === "ROLE_USER").length === 1  ? "selected" : "")
                        + ">USER</option>"
                    );
            }
        });
        $("#updateForm").submit(function () {
            $.ajax({
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                type: "PUT",
                url: updateUserUrl + id,
                dataType: 'json',
                data: JSON.stringify($(this).serializeObject())
            })

        })

    });


    //Delete
    $(document).on('click', '.dlt', function () {
        let id = parseInt($(this).attr('id').substr(7));
        $("#deleteModal").modal('show');

        $.ajax({
            url: getUserUrl + id,
            type: "GET",
            dataType: "json",
            success: function (response) {
                $("#fn_delete").val(response.firstName)
                $("#ln_delete").val(response.lastName)
                $("#age_delete").val(response.age)
                $("#email_delete").val(response.email)
                $("#password_delete").val(response.password)
                $("#roles_delete").empty().append(
                    "<option "
                    + (response.roles.filter(x => x.role === "ROLE_ADMIN").length === 1 ? "selected" : "")
                    + "> ADMIN </option> " +
                    "<option "
                    + (response.roles.filter(x => x.role === "ROLE_USER").length === 1  ? "selected" : "")
                    + ">USER</option>"
                );
            }
        });

        $("#deleteForm").submit(function () {
            $.ajax({
                type: "DELETE",
                url: deleteUserUrl + id,
            })

        })
    })
});

//Utils


let lineUser = function (user) {

    return "<td>" + user.id + "</td>" +
        "<td>" + (user.firstName==null ? "":user.firstName) + "</td> " +
        "<td>" + (user.lastName==null ? "":user.lastName) + "</td>" +
        "<td>" + (user.age==null ? "":user.age) + "</td>" +
        "<td>" + (user.email==null ? "":user.email) + "</td>" +
        "<td>" + getRolesFormat(user.roles) + "</td>";
}

let lineButtons = function (user) {
    return "<td><a class=\"btn btn-info upd\" data-toggle=\"modal\" id=\"update_" + user.id + "\" role=\"button\">Edit</a></td>" +
        "<td><a class=\"btn btn-danger dlt\" data-toggle=\"modal\" id=\"delete_" + user.id +"\" role=\"button\">Delete</a></td>";
}

$.fn.serializeObject = function() {
    let o = {};
    const a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

let getRolesFormat = function (roles) {
    let result = "";
    $.each(roles, function () {
        result = result + this.role.substr(5) + ", ";
    })
    return result.substr(0, result.length - 2);
}