<?php

//Get database information from db_info PHP file.
include ("db_info.php");

//Obtain values from frontend
$buy_rate = $_POST["buy"];
$sell_rate = $_POST["sell"];

//Prepare query to insert into DB "?" to avoid SQL injections
$query = $mysqli->prepare("INSERT INTO lira_rates (rate_lbp_buy, rate_lbp_sell) VALUES (?, ?);");

//Bind values to object
$query->bind_param("ss", $buy_rate, $sell_rate);

//Perform query
$query->execute();


?>