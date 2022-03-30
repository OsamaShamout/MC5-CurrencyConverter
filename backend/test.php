<?php 

//get database information
include ("db_info.php");

//prepare query to avoid SQL injections
$query = $mysqli->prepare("SELECT * FROM lira_rates;");

//perform query
$query->execute();

//store query in an array
$array = $query->get_result();

//initalize an array
$response = [];

//add for every row response array (an object lira rate into array)
while($lira_rate = $array->fetch_assoc()){
    //sav table
    $response[] = $lira_rate;
}


$json_response = json_encode($response);
 
echo $json_response;

?>