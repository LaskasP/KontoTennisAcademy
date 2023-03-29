const navToggle = document.querySelector(".navbar_toggle");
const links = document.querySelector(".main_nav");
const carouselImages = document.querySelector(".slideshow-container");

navToggle.addEventListener("click", function () {
  links.classList.toggle("show_nav");
});

let timer = setInterval(() => {
  plusSlides(1);
}, 5000);

let slideIndex = 1;
showSlides(slideIndex);

// Next/previous controls
function plusSlides(n) {
  showSlides((slideIndex += n));
}

// Thumbnail image controls
function currentSlide(n) {
  showSlides((slideIndex = n));
}

function showSlides(n) {
  let i;
  let slides = document.getElementsByClassName("mySlides");
  if (n > slides.length) {
    slideIndex = 1;
  }
  if (n < 1) {
    slideIndex = slides.length;
  }
  for (i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";
  }
  slides[slideIndex - 1].style.display = "block";
}

const headerTitle = document.querySelector(".logo-title");

function updateText() {
  if (window.innerWidth < 992) {
    headerTitle.textContent = "";
  } else {
    headerTitle.textContent = "KONTOPOULOS TENNIS ACADEMY";
  }
}

window.addEventListener("resize", updateText);
window.addEventListener("load", updateText);

carouselImages.addEventListener("mouseenter", () => {
  clearInterval(timer);
});

carouselImages.addEventListener("mouseleave", () => {
  timer = setInterval(() => {
    plusSlides(1);
  }, 5000);
});
