<?php

//THIS PHP FILE IS RESPONSIBLE FOR SENDING DATA INFO INTO MYSQL DB.

//Get database information from db_config PHP file.
include ("db_config.php");

//Obtain values from frontend.
$rate_lbp_buy = $_POST["buy"];
$rate_lbp_sell = $_POST["sell"];
$currency = $_POST["currency"];
$amount_req = $_POST["amount"];


$converted;

//Note using else in case in the future, different currencies were added.
//Conversion from USD to LBP
if ($currency == "USD"){
    $converted = floatval($rate_lbp_sell) * floatval($amount_req);
}

//Conversion from LBP to USD
if($currency == "LBP"){
    $converted = floatval($amount_req) / floatval($rate_lbp_buy);
}

//Prepare query to insert into DB "?" to avoid SQL injections
$query = $mysqli->prepare("INSERT INTO lira_rates (rate_lbp_buy, rate_lbp_sell, currency, amount_requested, amount_result) VALUES (?, ?, ?, ?, ?);");

//Bind values to object
$query->bind_param("sssss", $rate_lbp_buy, $rate_lbp_sell, $currency, $amount_req, $converted);

//Perform query
$query->execute();


?>