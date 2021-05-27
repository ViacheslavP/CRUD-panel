"use strict";

$(document).ready(function(){
    // Fill user table
    fillUser();

    // Fill admin table
    fillAdmin()

    // Submit new user
    $("#newUser").submit(function () {
        addUser($(this))
    })

    // Update user
    $(document).on('click', '.upd', function () {
        let id = parseInt($(this).attr('id').substr(7));
        $("#updateModal").modal('show');
        fillModal("edit", id)

        $("#updateForm").submit(function () {
            updateUser($(this), id)
        });
    });


    // Delete user
    $(document).on('click', '.dlt', function () {
        let id = parseInt($(this).attr('id').substr(7));
        $("#deleteModal").modal('show');
        fillModal("delete", id)

        $("#deleteForm").submit(function () {
            deleteUser(id)
        })
    });
});

//Utils

let fillModal = function (btn = "", id) {
    fetch("http://localhost:8080/api/admin/get/"+id, {method: "GET", credentials: "include"})
        .then(x => x.json())
        .then(response => {
            $("#fn_" + btn).val(response.firstName)
            $("#ln_" + btn).val(response.lastName)
            $("#age_" + btn).val(response.age)
            $("#email_" + btn).val(response.email)
            $("#password_" + btn).val(response.password)
            $("#roles_" + btn).empty().append(
                "<option "
                + (response.roles.filter(x => x.role === "ROLE_ADMIN").length === 1 ? "selected" : "")
                + "> ADMIN </option> " +
                "<option "
                + (response.roles.filter(x => x.role === "ROLE_USER").length === 1  ? "selected" : "")
                + ">USER</option>"
            );
        });
}

let deleteUser = function (id) {
    fetch("http://localhost:8080/api/admin/delete/" + id, {method: "DELETE", credentials: "include"}).then()
}

let updateUser = function (user, id) {
    fetch("http://localhost:8080/api/admin/update/" + id, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: "PUT",
        credentials: "include",
        body: JSON.stringify(user.serializeObject())
    }).then();
}

let addUser = function (user) {
    fetch("http://localhost:8080/api/admin/", {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: "POST",
        credentials: "include",
        body: JSON.stringify(user.serializeObject())
    }).then();
}

let fillAdmin = function () {
    fetch("http://localhost:8080/api/admin/", {
        method: "GET",
        credentials: "include"
    })
        .then(x=>x.json())
        .then(response => {
            let result = "";
            let len = response.length

            for (let i = 0; i < len; i++) {
                result = result + "<tr>" + lineUser(response[i]) + lineButtons(response[i]) + "</tr>"
            }
            $("#tableOfUsers").append(result);
        })
}

let fillUser = function () {
    fetch("http://localhost:8080/api/user", {
        method: "GET",
        credentials: "include"
    })
        .then(x => x.json())
        .then(response => {
            // Filling the header
            let head = "<strong>" + response.email + "</strong>" + " with roles " + getRolesFormat(response.roles);
            $("#navBarHeader").append(head);
            // Filling the usertable
            let line = "<tr>" + lineUser(response) + "</tr>";
            $("#userData").append(line);

        });
}

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