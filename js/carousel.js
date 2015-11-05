
function reset() {
  $("#perspective-icon").attr("src", "images/browser.png");
  $("#submission-icon").attr("src", "images/upload.png");
  $("#dataset-icon").attr("src", "images/compose.png");
}

function slideOn(number) {
  $("#preview").carousel(number);
  console.log("Slide on : " + number);
}

$("#preview").on('slide.bs.carousel', function(e) {
  reset();
  if (e.relatedTarget.id == "perspective-slide") {
    $("#perspective-icon").attr("src", "images/browser-hover.png");
  }
  if (e.relatedTarget.id == "submission-slide") {
    $("#submission-icon").attr("src", "images/upload-hover.png");
  }
  else if (e.relatedTarget.id == "dataset-slide") {
    $("#dataset-icon").attr("src", "images/compose-hover.png");
  }

});
