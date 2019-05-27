<?php
include 'dbconfig.php';

$title = $_GET['title'];
$text = $_GET['text'];
$author_id = $_GET['author_id'];
$timestamp = $_GET['timestamp'];
$model = $_GET['model'];
$brand = $_GET['brand'];
$sdk = $_GET['sdk'];
$version = $_GET['version'];
$product = $_GET['product'];

//get the author full name
$sql = "SELECT name FROM psu_admin WHERE psu_id = '$author_id'";

$result = $conn->query($sql);
$result_assoc = $result->fetch_assoc();
$author_name = $result_assoc['name'];

$sql = "INSERT INTO psu_feedback (title, text, author_name, timestamp, model, brand, sdk, version, product) VALUES ('$title','$text','$author_name','$timestamp','$model','$brand','$sdk','$version','$product')";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}
?>