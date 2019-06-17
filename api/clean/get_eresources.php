<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "SELECT * FROM psu_eresources WHERE category = '$id'";

$arr = [];
$counter = 0;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {

		$arr[$counter]['text'] = $row['title'];
		$arr[$counter]['id'] = $row['url'];

		$counter++;
 	}
}

echo json_encode($arr);

?>