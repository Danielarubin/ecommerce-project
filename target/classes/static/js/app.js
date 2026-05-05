document.addEventListener('DOMContentLoaded', () => {
    // === ESTADO ===
    let cart = [];
    let favorites = [];

    // === ELEMENTOS DOM ===
    const cartDrawer = document.getElementById('cartDrawer');
    const drawerOverlay = document.getElementById('drawerOverlay');
    const searchOverlay = document.getElementById('searchOverlay');
    const searchInput = document.getElementById('searchInput');
    const searchResults = document.getElementById('searchResults');
    const cartItemsContainer = document.getElementById('cartItems');
    const cartTotalValue = document.getElementById('cartTotalValue');
    const cartEmpty = document.getElementById('cartEmpty');
    const cartBadge = document.getElementById('cartBadge');

    // === INICIALIZACIÓN DE DATOS DEL BACKEND ===
    async function loadInitialData() {
        try {
            const [cartRes, favRes] = await Promise.all([
                fetch('/api/cart'),
                fetch('/api/favorites')
            ]);
            
            if (cartRes.ok) cart = await cartRes.json();
            if (favRes.ok) favorites = await favRes.json();
            
            updateCartUI();
            initializeFavorites();
        } catch (error) {
            console.error("Error loading initial data", error);
        }
    }

    // === LISTENERS BÁSICOS ===
    document.getElementById('btnCart')?.addEventListener('click', () => toggleDrawer(cartDrawer, true));
    document.getElementById('btnCloseCart')?.addEventListener('click', () => toggleDrawer(cartDrawer, false));
    
    document.getElementById('btnSearch')?.addEventListener('click', () => {
        searchOverlay.classList.add('active');
        searchInput.focus();
    });
    document.getElementById('btnCloseSearch')?.addEventListener('click', () => {
        searchOverlay.classList.remove('active');
        searchInput.value = '';
        searchResults.innerHTML = '';
    });
    
    drawerOverlay?.addEventListener('click', () => toggleDrawer(cartDrawer, false));

    function toggleDrawer(drawer, show) {
        if (show) {
            drawer.classList.add('open');
            drawerOverlay.classList.add('active');
        } else {
            drawer.classList.remove('open');
            drawerOverlay.classList.remove('active');
        }
    }

    // === LÓGICA DE FAVORITOS (BACKEND) ===
    function initializeFavorites() {
        document.querySelectorAll('.product-card').forEach(card => {
            const id = card.dataset.id;
            const btnFav = card.querySelector('.btn-fav');
            if (!btnFav) return;
            
            if (favorites.includes(id)) {
                btnFav.textContent = '🖤';
                btnFav.classList.add('active');
            }

            // Remove previous event listeners if any to avoid duplicates
            const newBtnFav = btnFav.cloneNode(true);
            btnFav.parentNode.replaceChild(newBtnFav, btnFav);

            newBtnFav.addEventListener('click', async (e) => {
                e.preventDefault();
                e.stopPropagation(); 
                
                // Optimistic UI update
                const isActive = newBtnFav.classList.contains('active');
                if (isActive) {
                    newBtnFav.textContent = '🤍';
                    newBtnFav.classList.remove('active');
                } else {
                    newBtnFav.textContent = '🖤';
                    newBtnFav.classList.add('active');
                }

                try {
                    const formData = new URLSearchParams();
                    formData.append('id', id);
                    const res = await fetch('/api/favorites/toggle', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                        body: formData.toString()
                    });
                    if (res.ok) {
                        favorites = await res.json();
                    }
                } catch (err) {
                    console.error("Error toggling favorite", err);
                    // Revert on error
                }
            });
        });
    }

    // === LÓGICA DE CARRITO (BACKEND) ===
    document.querySelectorAll('.product-card').forEach(card => {
        const btnAdd = card.querySelector('.btn-add-cart');
        if (!btnAdd) return;

        // Clone to remove old listeners
        const newBtnAdd = btnAdd.cloneNode(true);
        btnAdd.parentNode.replaceChild(newBtnAdd, btnAdd);

        newBtnAdd.addEventListener('click', async (e) => {
            e.preventDefault();
            e.stopPropagation(); 
            
            const product = {
                id: card.dataset.id,
                name: card.dataset.name,
                brand: card.dataset.brand,
                price: parseFloat(card.dataset.price.replace(/[$,]/g, '')),
                image: card.dataset.image,
                qty: 1
            };
            
            toggleDrawer(cartDrawer, true); 
            await addToCart(product);
        });
    });

    async function addToCart(product) {
        // Optimistic update
        const existing = cart.find(item => item.id === product.id);
        if (existing) {
            existing.qty += 1;
        } else {
            cart.push({ ...product, qty: 1 });
        }
        updateCartUI();

        try {
            const res = await fetch('/api/cart/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(product)
            });
            if (res.ok) {
                cart = await res.json();
                updateCartUI();
            }
        } catch (err) {
            console.error("Error adding to cart", err);
        }
    }

    window.updateQty = async function(index, delta) {
        // Optimistic update
        cart[index].qty += delta;
        if (cart[index].qty <= 0) {
            cart.splice(index, 1);
        }
        updateCartUI();

        try {
            const formData = new URLSearchParams();
            formData.append('index', index);
            formData.append('delta', delta);
            
            const res = await fetch('/api/cart/update', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: formData.toString()
            });
            
            if (res.ok) {
                cart = await res.json();
                updateCartUI();
            }
        } catch (err) {
            console.error("Error updating qty", err);
        }
    };

    function updateCartUI() {
        if (!cartItemsContainer) return;
        
        cartItemsContainer.innerHTML = '';
        let total = 0;
        let count = 0;

        if (cart.length === 0) {
            cartItemsContainer.appendChild(cartEmpty);
            cartEmpty.style.display = 'block';
        } else {
            if(cartEmpty) cartEmpty.style.display = 'none';
            cart.forEach((item, index) => {
                total += item.price * item.qty;
                count += item.qty;

                const el = document.createElement('div');
                el.className = 'cart-item';
                el.innerHTML = `
                    <img src="/images/${item.image}" alt="${item.name}">
                    <div class="cart-item-info">
                        <h4>${item.name}</h4>
                        <p>${item.brand}</p>
                        <div class="cart-item-price">$${item.price.toLocaleString('es-MX')}</div>
                        <div class="cart-item-qty">
                            <button onclick="updateQty(${index}, -1)">-</button>
                            <span>${item.qty}</span>
                            <button onclick="updateQty(${index}, 1)">+</button>
                        </div>
                    </div>
                `;
                cartItemsContainer.appendChild(el);
            });
        }

        if(cartTotalValue) cartTotalValue.textContent = `$${total.toLocaleString('es-MX')}`;
        if (cartBadge) {
            cartBadge.textContent = count;
            cartBadge.style.display = count > 0 ? 'flex' : 'none';
        }
    }

    // === LÓGICA DE BUSCADOR (Frontend, sin cambios) ===
    searchInput?.addEventListener('input', (e) => {
        const query = e.target.value.toLowerCase().trim();
        searchResults.innerHTML = '';

        if (query.length === 0) return;

        const cards = document.querySelectorAll('.product-card');
        let resultsCount = 0;

        cards.forEach(card => {
            const name = card.dataset.name.toLowerCase();
            const brand = card.dataset.brand.toLowerCase();
            if (name.includes(query) || brand.includes(query)) {
                resultsCount++;
                const clone = card.cloneNode(true);
                clone.className = 'search-result-item';
                clone.style.display = 'flex';
                clone.style.alignItems = 'center';
                clone.style.gap = '1.5rem';
                clone.style.borderBottom = '1px solid var(--border-color)';
                clone.style.paddingBottom = '1rem';
                
                const img = clone.querySelector('.e-img');
                if(img) {
                    img.style.width = '80px';
                    img.style.height = '100px';
                    img.style.padding = '0';
                    img.style.background = 'transparent';
                    const imgTag = img.querySelector('img');
                    if(imgTag) {
                        imgTag.style.objectFit = 'cover';
                        imgTag.style.width = '100%';
                        imgTag.style.height = '100%';
                        imgTag.style.padding = '0';
                    }
                }
                
                const btnF = clone.querySelector('.btn-fav');
                if(btnF) btnF.remove();
                const btnC = clone.querySelector('.btn-add-cart');
                if(btnC) btnC.remove();

                searchResults.appendChild(clone);
            }
        });

        if (resultsCount === 0) {
            searchResults.innerHTML = '<p style="color:var(--text-muted); font-size:1.2rem;">No se encontraron piezas que coincidan con tu búsqueda.</p>';
        }
    });

    // Cargar datos iniciales del backend al iniciar la página
    loadInitialData();
});
