<?php 

//GETTING INFO FROM DB

//Get database information from db_info PHP file.
include ("db_config.php");

//Prepare query to avoid SQL injections
//Get the last query to match user conversion amount.
$query = $mysqli->prepare("SELECT * FROM lira_rates ORDER BY id DESC LIMIT 1;");

//Perform query
$query->execute();

//Store query in an array
$array = $query->get_result();

//Initalize an array
$response = [];

//Add for every row response array (an object lira rate into array)
while($lira_rate = $array->fetch_assoc()){
    //Save table rows
    $response[] = $lira_rate;
}

//Encode the response into a JSON object.
$json_response = json_encode($response);
 
//Display the JSON Object resulted. (Testing)
echo $json_response;

?>