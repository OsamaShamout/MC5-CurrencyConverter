<?php

//ADDING INFO TO DB

//Get database information from db_info PHP file.
include ("db_info.php");

//Obtain values from frontend
$rate_lbp_buy = $_POST["rate_lbp_buy"];
$rate_lbp_sell = $_POST["rate_lbp_sell"];
//$current_time_stamp = current_timestamp();

//Prepare query to insert into DB "?" to avoid SQL injections
$query = $mysqli->prepare("INSERT INTO lira_rates (rate_lbp_buy, rate_lbp_sell) VALUES (?, ?);");

//Bind values to object
$query->bind_param("ss", $rate_lbp_buy, $rate_lbp_sell);

//Perform query
$query->execute();

$response = [];

$response["Status"] = "Mabrouk!";

$json_response = json_encode($response);

echo $json_response;

?>