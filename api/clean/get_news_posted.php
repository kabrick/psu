<?php
include 'dbconfig.php';

$author = $_GET['name'];

$sql = "SELECT * FROM psu_news WHERE author = '$author' ORDER BY id DESC";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row[] = $result->fetch_assoc()) {
 		$json = json_encode($row);
 	}
} else {
	//
}

echo $json;

?>