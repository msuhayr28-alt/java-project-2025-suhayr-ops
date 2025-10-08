# 🦖Wallace adventure

## 🎮 Overview
**Wallace adventure** is a **physics-based 2D platformer** built entirely in **Java** using the **CityEngine physics library**.  
The player controls a small robot navigating through platforms, collecting stars, and avoiding hazards in a realistic physics-driven environment.  
Gravity, collisions, friction, and impulse-based movement all play a central role in the gameplay, making the mechanics feel natural and dynamic.

Developed as part of the **IN1007 – Object-Oriented Programming coursework**, this project demonstrates advanced programming techniques, including inheritance, encapsulation, polymorphism, event-driven design, and applied physics simulation.

---

## ⚙️ Physics Features

- **CityEngine Physics Integration**  
  Every object in the world — including the player, enemies, platforms, and projectiles — is a physical body with mass, friction, and restitution, fully simulated by CityEngine.

- **Realistic Gravity and Jumping**  
  The player’s jump and fall behavior are determined by gravity scaling, ensuring consistent vertical motion and collision detection with platforms.

- **Collision Listeners**  
  Used extensively for:
  - Detecting player-star collisions (to collect points)
  - Projectile impacts with the player
  - Player-landed detection via foot sensors

- **Sensors and Fixtures**  
  The player uses **sensor fixtures** to detect ground contact and prevent mid-air jumping.  
  Platforms and enemies use **static and dynamic bodies** to react physically to the player and environment.

- **Impulse and Velocity Control**  
  Movement, jumping, and projectile shooting rely on linear velocity and impulses rather than artificial position updates — maintaining physical realism.

---

## 🧩 Game Features

- **Three Physically Unique Levels**
  - **Level 1:** Basic platformer physics and collectibles.
  - **Level 2:** Ice level with slippery surfaces (reduced friction) and falling spikes.
  - **Level 3:** Fire level with projectile-shooting enemies and faster dynamics.

- **Dynamic Player Controls**
  Smooth acceleration and deceleration, based on physics forces.

- **Collectible Stars**
  Trigger sound effects, increase score, and unlock higher levels.

- **AI Enemies**
  Use projectile physics to fire at the player and simulate patrol or chase movement.

- **Sound & Feedback**
  Impact sounds and ambient background music add to immersion.

- **Game States**
  Includes **Game Over**, **Level Complete**, and **You Win** transitions.

---

## 🧠 Challenges & Lessons Learned

During development, key challenges included:
- Achieving realistic **jumping and collision responses** while maintaining fun gameplay.
- Managing multiple **physics listeners** (collisions, sensors, steps) without causing event conflicts.
- Implementing AI enemies that **use physical projectiles** instead of teleporting attacks.
- Handling **dynamic object creation** while maintaining consistent physics performance.

**Key Lessons Learned:**
- The importance of **physics tuning** (mass, density, friction) for gameplay balance.
- Using **event-driven physics** instead of manual position manipulation.
- Designing an extensible class hierarchy that reuses physics logic efficiently.

---

## 🕹️ Controls

| Key | Action |
|-----|--------|
| ← / → | Move left / right |
| ↑ / Space | Jump |

---
