<?php

//MySQL Host Name.
$db_host = "localhost";

//MySQL Database Username.
$db_user = "id18699797_mc_osama";

//MySQL Database password.
$db_password = "7e~])+ey>FmG4W3O";

//MySQL Database Name.
$db_name = "id18699797_mobile_db";

//Object-oriented instantiation for the MySQL DB.
$mysqli = new mysqli($db_host,$db_user,$db_password,$db_name);

//Display a connection error if DB failed to connect.
if(mysqli_connect_errno()){
    die("Connection failed.");
}


?>
