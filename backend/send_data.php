<?php

//THIS PHP FILE IS RESPONSIBLE FOR SENDING DATA INFO MYSQL DB.

//Get database information from db_config PHP file.
include ("db_config.php");

//Obtain values from frontend
$rate_lbp_buy = $_POST["buy"];
$rate_lbp_sell = $_POST["sell"];
$currency = $_POST["currency"];
$amount_req = $_POST["amount"];

$converted;


//Performing conversion currency conversion
//Strcasecmp in case USD or LBP was changed in its capitalization later on.
if (strcasecmp($currency, "USD") == 0){
    $converted = floatval($rate_lbp_sell) * floatval($amount_req);
}
else if(strcasecmp($currency, "LBP")){
    $converted = floatval($amount_req) / floatval($rate_lbp_buy);
}


//Prepare query to insert into DB "?" to avoid SQL injections
$query = $mysqli->prepare("INSERT INTO lira_rates (rate_lbp_buy, rate_lbp_sell, currency, amount_requested, amount_result) VALUES (?, ?, ?, ?, ?);");

//Bind values to object
$query->bind_param("sssss", $rate_lbp_buy, $rate_lbp_sell, $currency, $amount_req, $converted);

//Perform query
$query->execute();


?>