<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "SELECT c.comment, c.timestamp, a.photo, a.name AS author FROM psu_admin a, psu_news_comments c WHERE c.news_id = '$id' AND c.psu_id = a.psu_id ORDER BY id DESC";

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