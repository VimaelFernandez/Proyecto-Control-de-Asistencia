
function loadTable(dataUsers) {
    console.log(dataUsers);
    var trHTML = '';
    for (const dataUser of dataUsers) {
        trHTML += '<tr>';
        trHTML += '<td>' + dataUser.id + '</td>';
        trHTML += '<td>' + dataUser.name + '</td>';
        trHTML += '<td>' + dataUser.lastName + '</td>';
        trHTML += '<td>' + dataUser.department + '</td>';
        trHTML += '<td>' + dataUser.jobRole + '</td>';
        trHTML += '<td><button type="button" class="btn btn-outline-secondary" onclick="showUserEditBox(' + dataUser.id + ')">Edit</button>';
        trHTML += '<button type="button" class="btn btn-outline-danger" onclick="userDelete(' + dataUser.id + ')">Del</button></td>';
        trHTML += "</tr>";
    }
    document.getElementById("mytable").innerHTML = trHTML;
}

function showSystemEmpty() {
    var trHTML = '';
    trHTML += '<tr>';
    trHTML += '<td>' + '-' + '</td>';
    trHTML += '<td>' + '-' + '</td>';
    trHTML += '<td>' + '-' + '</td>';
    trHTML += '<td>' + '-' + '</td>';
    trHTML += '<td>' + '-' + '</td>';
    trHTML += '<td>' + '-' + '</td>';
    trHTML += "</tr>";
    document.getElementById("mytable").innerHTML = trHTML;
}

async function getDataUser(url) {
    try {
        const response = await fetch(url);
        switch (response.status) {
            case 204:
                showSystemEmpty();
                break;
            case 200:
                const dataUser = await response.json();
                loadTable(dataUser);
                break;
            default:
                throw new Error(`Error en la solicitud: ${response.status}`);
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

getDataUser('http://192.168.0.100:8080/user/findAll')



function showUserCreateBox() {
    Swal.fire({
        title: 'Create user',
        html: `
    <input id="id" name="userId" type="hidden">
    <input id="fname" name="NameUser" class="swal2-input" placeholder="First">
    <input id="lname"  name="LastNameUser" class="swal2-input" placeholder="Last">
    <input id="area" name="Area" class="swal2-input" placeholder="Area">
    <input id="role" name="Role" class="swal2-input" placeholder="Role">
`
        ,
        focusConfirm: false,
        preConfirm: () => {
            userCreate();
        }
    })
}

function userCreate() {

    const data = [{
        type: "user",
        name: document.getElementById("fname").value,
        lastName: document.getElementById("lname").value,
    },
    {
        type: "department",
        department: document.getElementById("area").value
    },
    {
        type: "role",
        role: document.getElementById("role").value
    }
    ]
    console.log(data);

    // Realizamos la solicitud POST
    fetch(`http://192.168.0.100:8080/user/create`, {
        method: 'POST', // Especificamos el método HTTP
        headers: {
            'Content-Type': 'application/json', // Tipo de contenido. Formato.
        },
        body: JSON.stringify(data) // Convertir objeto JSON a String
    })
        .then(response => {
            if (response.ok) {
                console.log('Recurso guardado con exito');
                Swal.fire('Recurso guardado con éxito');
                getDataUser('http://192.168.0.100:8080/user/findAll')
            } else {
                console.error('Error al eliminar el recurso:', response.status);
            }
        })
        .catch(error => {
            console.error('Ocurrió un error:', error);
        });

}
function userDelete(id) {

    // Realizamos la solicitud DELETE
    fetch(`http://192.168.0.100:8080/user/delete/${id}`, {
        method: 'DELETE', // Especificamos el método HTTP
        headers: {
            'Content-Type': 'application/json', // Opcional, pero puede ser útil
        }
    })
        .then(response => {
            if (response.ok) {
                console.log('Recurso eliminado con éxito');
                Swal.fire('Recurso eliminado con éxito');
                getDataUser('http://192.168.0.100:8080/user/findAll')
            } else {
                console.error('Error al eliminar el recurso:', response.status);
            }
        })
        .catch(error => {
            console.error('Ocurrió un error:', error);
        });
}

function userDelete(id) {

    // Realizamos la solicitud DELETE
    fetch(`http://192.168.0.100:8080/user/delete/${id}`, {
        method: 'DELETE', // Especificamos el método HTTP
        headers: {
            'Content-Type': 'application/json', // Opcional, pero puede ser útil
        }
    })
        .then(response => {
            if (response.ok) {
                console.log('Recurso eliminado con éxito');
                Swal.fire('Recurso eliminado con éxito');
                getDataUser('http://192.168.0.100:8080/user/findAll')
            } else {
                console.error('Error al eliminar el recurso:', response.status);
            }
        })
        .catch(error => {
            console.error('Ocurrió un error:', error);
        });
}


