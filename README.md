# Brewkery — Android Ordering App

A native Android coffee & bakery ordering application built in Kotlin using MVVM and Clean Architecture principles. Powered by REST API endpoints for store catalog data and single-item customizations, featuring live favorites filtering, dynamic item customization, an in-memory cart engine, and order status tracking.

---

## 📱 Application Flow & Features

```text
[Screen 1: Home / Menu] ──(Tap Item)──> [Screen 2: Item Detail]
         │                                       │ (Add to Cart)
         └──(Bottom Cart Bar)──────────────────> [Screen 3: Cart]
                                                         │ (Place Order)
                                                         v
                                                [Screen 4: Order Status]
```

- **Screen 1 — Home / Menu Catalog:**
  - Displays store announcements, operational status, delivery fee, and estimated delivery time.
  - Horizontally scrollable category chips including "All Items", category filters (e.g., "Hot Coffee", "Cold Brews"), and a dedicated **"Favourites"** filter tab.
  - Menu items list showing badges, thumbnails, prep times, ratings, and base prices.
  - Sticky bottom cart bar reflecting real-time item counts and subtotal.
- **Screen 2 — Item Detail & Customizer:**
  - Hero image card with an interactive heart button to toggle favorites.
  - Detailed product specs (prep time, calories, ingredients, descriptions).
  - Dynamic customization options (`sizes`, `milk_options`, `sugar_levels`) with live unit price recalculation.
- **Screen 3 — Cart & Breakdown:**
  - Itemized cart entries with selected modifiers, unit pricing, and quantity adjustments.
  - Automated cost breakdown: Subtotal, flat $2.50 delivery fee, 8.0% tax, and grand total.
- **Screen 4 — Order Status:**
  - Generates a unique order ticket ID (e.g., `#BK-74921`) with `PREPARING` badge.
  - Resets the active cart and returns user to the main menu with active order tracking.

---

## 🛠 Tech Stack & Architecture

- **Language:** Kotlin
- **Architecture Pattern:** MVVM (Model-View-ViewModel) + Clean Architecture (Data, Domain, Presentation layers)
- **Networking:** Retrofit 2 + Moshi / Gson + Kotlin Coroutines
- **Image Loading:** Coil (with shimmer placeholders and network error fallbacks)
- **State Management:** Kotlin `StateFlow` / `SharedFlow`
- **Testing:** JUnit 4 (Local JVM unit tests)

---

## 🧪 Unit Testing

Unit tests run locally on the JVM without requiring an emulator or physical device:

```bash
./gradlew test
```

### Test Suite: `CartRepositoryTest.kt`
- **`addToCart_increasesQuantity_whenSameItemAdded`**: Verifies that adding an item already present in the cart with identical customization options increments its quantity and recalculates the total price (e.g., Medium Espresso totaling $8.00) rather than creating a duplicate row entry.
- **`removeFromCart_removesItemSuccessfully`**: Confirms that removing an item updates the repository state cleanly and clears it from `cartItems.value`.

---

## 🤖 AI Usage & Workflow

In accordance with the assignment guidelines, this project leveraged AI tools throughout architecture scaffolding, UI implementation, and debugging.

### 1. AI Tools Used
- **Gemini AI**
- **Android Studio AI Companion**
- **GitHub Copilot**

### 2. Prompts Sent

- **Prompt 1 (Screen Architecture & Flow):**
  > *"The app is an ordering flow composed of 4 screens:*  
  > *[Screen 1: Home / Menu] -> [Screen 2: Item Detail] -> [Screen 3: Cart] -> [Screen 4: Order Status]*  
  > *this is the application I am going to build which has these four screens , build a native Android coffee & bakery ordering app in Kotlin powered by a single REST API. Load all images from their URLs with Coil or Glide. Show a loading state and an error message for the network call. The raw API endpoints (data.json for full menu & api/items/{id}.json for single items)."*

- **Prompt 2 (Architecture & Networking Setup):**
  > *"I want clean architecture and MVVM. Likewise suggest me the package structure. Also add Retrofit + coroutines and fetch the JSON from the API URL below.*  
  > *1. Full Menu & Store Config-https://raw.githubusercontent.com/VivekShah138/Brewkery/main/data.json*  
  > *2. Single Item Detail by ID-https://raw.githubusercontent.com/VivekShah138/Brewkery/main/api/items/{id}.json"*

- **Prompt 3 (Customization & Favorites Feature):**
  > *"I want the item image card in item detail screen like the screenshot I provided below with a favourites button and a heart graphic. Also in the menu catalog screen in the scrollable chipgroup that contains the "All items","hot coffee", etc add the "favourites" and add the functionality to it like when user toggle to favourites the items below are only the selected favourites."*

### 3. What AI Got Right
- Correctly organized the codebase into a clean MVVM structure separating domain models, repository implementations, and presentation view models.
- Scaffolded Retrofit service interfaces and suspended coroutine API calls handling both full menu payload and path-variable item details (`api/items/{id}.json`).

### 4. What AI Got Wrong & How It Was Fixed
- **Issue 1 (Static Horizontal ChipGroup):** The AI-generated XML/UI layout produced a `ChipGroup` that remained fixed and truncated category chips on smaller screen widths because it omitted horizontal scrolling containers and proper wrap constraints.  
  * **Fix:** Wrapped the `ChipGroup` inside a `HorizontalScrollView` (setting `android:scrollbars="none"` and `app:singleLine="true"`), allowing smooth, fluid horizontal scrolling across all categories.
- **Issue 2 (Favorites Filtering Logic):** The AI initially failed to wire the "Favourites" chip into the catalog filter pipeline, only handling basic category ID matches.  
  * **Fix:** Updated the filter logic in the `MenuViewModel` to track a reactive `Set<Int>` of favorited item IDs. When the "Favourites" chip is selected, the list filters strictly by `favoriteIds.contains(item.id)`, instantly updating the observed menu list.

---

## 📦 How to Build the Debug APK

Run the Gradle assemble command from the terminal:
```bash
./gradlew assembleDebug
```

The output `.apk` file will be generated at:
```text
app/build/outputs/apk/debug/app-debug.apk
```
