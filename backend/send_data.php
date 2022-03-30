<?php

//THIS PHP FILE IS RESPONSIBLE FOR SENDING DATA INFO MYSQL DB.

//Get database information from db_config PHP file.
include ("db_info.php");

//Obtain values from frontend
$rate_lbp_buy = $_POST["buy"];
$rate_lbp_sell = $_POST["sell"];

//Prepare query to insert into DB "?" to avoid SQL injections
$query = $mysqli->prepare("INSERT INTO lira_rates (rate_lbp_buy, rate_lbp_sell) VALUES (?, ?);");

//Bind values to object
$query->bind_param("ss", $rate_lbp_buy, $rate_lbp_sell);

//Perform query
$query->execute();


?>