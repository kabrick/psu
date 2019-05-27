<?php
include 'dbconfig.php';
$id = $_GET['id'];

$sql = "SELECT * FROM psu_news WHERE author = '$id' AND approved = '1' ORDER BY id DESC";

$arr = [];
$counter = 0;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {
		// get user name using the psu_id

		$id = $row['author'];

		//get the author full name
		$sql1 = "SELECT name FROM psu_admin WHERE psu_id = '$id'";

		$result1 = $conn->query($sql1);
		$result_assoc1 = $result1->fetch_assoc();

		$arr[$counter]['id'] = $row['id'];
		$arr[$counter]['title'] = $row['title'];
		$arr[$counter]['text'] = $row['text'];
		$arr[$counter]['timestamp'] = $row['timestamp'];

		// check if photo exists
		if(!is_null($row['photo'])){
			$arr[$counter]['image'] = $row['photo'];
		} else {
			$arr[$counter]['image'] = 0;
		}

		$arr[$counter]['author'] = $result_assoc1['name'];

		$counter++;
 	}
} else {
	//
}

echo json_encode($arr);
?>