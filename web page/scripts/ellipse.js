// create scene
const scene = new THREE.Scene();
const camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
const renderer = new THREE.WebGLRenderer({ antialias: true });

renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);

// ellipse geometry (scaled sphere)
const geometry = new THREE.SphereGeometry(1, 64, 32);
geometry.scale(2.0, 1.0, 1.5); // make it ellipse-like
const material = new THREE.MeshStandardMaterial({ color: 0xffffff });
const ellipse = new THREE.Mesh(geometry, material);
scene.add(ellipse);

// lights
const light = new THREE.PointLight(0xffffff, 1);
light.position.set(5, 5, 5);
scene.add(light);
scene.add(new THREE.AmbientLight(0x404040));

// camera position
camera.position.z = 5;

// floating animation
let t = 0;
function animate() {
    requestAnimationFrame(animate);
    t += 0.02;
    ellipse.rotation.y += 0.01;
    ellipse.position.y = Math.sin(t) * 0.5;
    renderer.render(scene, camera);
}
animate();

window.addEventListener('resize', () => {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
});
