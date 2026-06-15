let navbar = document.querySelector('.header .flex .navbar');

function scrollvalue(){
   var navbar= document.getElementById('headerflex');
   var scroll = window.scrollY;
   if(scroll < 300){
      navbar.classList.remove('BgColourofnavbar');
   }
   else{
      navbar.classList.add('BgColourofnavbar');
   }
}

window.addEventListener('scroll' ,scrollvalue);


let slides = document.querySelectorAll('.home-bg .home .slide-container .slide');
let index =0;

function next() {
   slides[index].classList.remove('active');
   index= (index+1)%slides.length;
   slides[index].classList.add('active');
}

function prev() {
   slides[index].classList.remove('active');
   index= (index-1 +slides.length)%slides.length;
   slides[index].classList.add('active');
}



let faqindi = document.querySelectorAll('.faq .faq-container .faqindi');

faqindi.forEach(acco =>{
   acco.onclick = () =>{
      faqindi.forEach(remove => remove.classList.remove('active'));
      acco.classList.add('active');
   }
});