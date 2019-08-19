<?php
include 'dbconfig.php';

// get sent data
$owner_id = $_GET['owner_id'];
$pharmacy_id = $_GET['pharmacy_id'];
$pharmacist_id = $_GET['pharmacist_id'];
$appraiser_name = $_GET['appraiser_name'];
$appraiser_title = $_GET['appraiser_title'];
$from_period = $_GET['from_period'];
$to_period = $_GET['to_period'];
$score_one = $_GET['score_one'];
$score_two = $_GET['score_two'];
$score_three = $_GET['score_three'];
$score_four = $_GET['score_four'];
$score_five = $_GET['score_five'];
$average_score = $_GET['average_score'];
$remarks = $_GET['remarks'];
$timestamp = $_GET['timestamp'];

//insert values
$sql = "INSERT INTO psu_pharmacist_assessment (owner_id, pharmacy_id, pharmacist_id, appraiser_name, appraiser_title, from_period, to_period, score_one, score_two, score_three, score_four, score_five, average_score, remarks, timestamp) VALUES ('$owner_id','$pharmacy_id','$pharmacist_id','$appraiser_name','$appraiser_title','$from_period','$to_period','$score_one','$score_two','$score_three','$score_four','$score_five','$average_score','$remarks','$timestamp')";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>