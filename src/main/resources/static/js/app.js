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

    // === CONFIRMACIONES GLOBALES ===
    function ensureConfirmDialog() {
        let backdrop = document.getElementById('appConfirmBackdrop');
        if (backdrop) return backdrop;

        backdrop = document.createElement('div');
        backdrop.id = 'appConfirmBackdrop';
        backdrop.className = 'app-confirm-backdrop';
        backdrop.innerHTML = `
            <div class="app-confirm-dialog" role="dialog" aria-modal="true" aria-labelledby="appConfirmTitle">
                <div class="app-confirm-kicker">Confirmacion</div>
                <h2 class="app-confirm-title" id="appConfirmTitle">Confirmar accion</h2>
                <p class="app-confirm-message" id="appConfirmMessage"></p>
                <div class="app-confirm-actions">
                    <button type="button" class="app-confirm-btn cancel" id="appConfirmCancel">Cancelar</button>
                    <button type="button" class="app-confirm-btn confirm danger" id="appConfirmAccept">Confirmar</button>
                </div>
            </div>
        `;
        document.body.appendChild(backdrop);
        return backdrop;
    }

    // === MODAL DE TALLA ===
    function ensureSizeModal() {
        let modal = document.getElementById('sizeModal');
        if (modal) return modal;
        modal = document.createElement('div');
        modal.id = 'sizeModal';
        modal.className = 'app-confirm-backdrop';
        modal.innerHTML = `
            <div class="app-confirm-dialog" role="dialog" aria-modal="true" style="max-width:420px">
                <div class="app-confirm-kicker">Selecciona tu talla</div>
                <h2 class="app-confirm-title" id="sizeModalProductName" style="font-size:1.4rem;margin-bottom:0.5rem;"></h2>
                <p style="font-size:0.82rem;color:var(--text-muted,#8B7355);margin-bottom:1.5rem;">
                    Elige la talla antes de añadir a la bolsa.
                </p>
                <div id="sizeModalGrid" style="display:flex;flex-wrap:wrap;gap:0.5rem;margin-bottom:1.75rem;"></div>
                <div style="display:grid;grid-template-columns:1fr 1fr;gap:0.75rem;">
                    <button type="button" id="sizeModalCancel"
                        style="padding:0.85rem;border:1px solid var(--border-color,rgba(42,33,24,0.1));border-radius:4px;background:transparent;cursor:pointer;font-size:0.82rem;">Cancelar</button>
                    <button type="button" id="sizeModalConfirm" disabled
                        style="padding:0.85rem;background:var(--text-dark,#1C1410);color:#FDFAF5;border:none;border-radius:4px;cursor:pointer;font-size:0.82rem;font-weight:500;">Añadir a bolsa</button>
                </div>
            </div>
        `;
        document.body.appendChild(modal);
        return modal;
    }

    function openSizeModal(sizes, productName) {
        return new Promise(resolve => {
            const modal = ensureSizeModal();
            document.getElementById('sizeModalProductName').textContent = productName || 'Prenda';
            const grid = document.getElementById('sizeModalGrid');
            const confirmBtn = document.getElementById('sizeModalConfirm');
            const cancelBtn = document.getElementById('sizeModalCancel');
            grid.innerHTML = '';
            confirmBtn.disabled = true;
            let selected = null;

            sizes.forEach(size => {
                const btn = document.createElement('button');
                btn.type = 'button';
                btn.textContent = size;
                btn.style.cssText = 'min-width:52px;padding:0.45rem 0.85rem;border:1px solid var(--border-color,rgba(42,33,24,0.1));border-radius:999px;background:#FFFDF9;cursor:pointer;font-size:0.85rem;font-weight:700;transition:all 0.2s;';
                btn.addEventListener('click', () => {
                    grid.querySelectorAll('button').forEach(b => {
                        b.style.background = '#FFFDF9';
                        b.style.borderColor = 'var(--border-color,rgba(42,33,24,0.1))';
                        b.style.color = 'inherit';
                    });
                    btn.style.background = 'var(--text-dark,#1C1410)';
                    btn.style.borderColor = 'var(--text-dark,#1C1410)';
                    btn.style.color = '#FDFAF5';
                    selected = size;
                    confirmBtn.disabled = false;
                });
                grid.appendChild(btn);
            });

            const close = value => {
                modal.classList.remove('active');
                cancelBtn.removeEventListener('click', onCancel);
                confirmBtn.removeEventListener('click', onConfirm);
                modal.removeEventListener('click', onBackdrop);
                resolve(value);
            };
            const onCancel = () => close(null);
            const onConfirm = () => close(selected);
            const onBackdrop = e => { if (e.target === modal) close(null); };

            cancelBtn.addEventListener('click', onCancel);
            confirmBtn.addEventListener('click', onConfirm);
            modal.addEventListener('click', onBackdrop);

            modal.classList.add('active');
        });
    }

    function openConfirm({ title, message, confirmText, danger }) {
        return new Promise(resolve => {
            const backdrop = ensureConfirmDialog();
            const titleEl = backdrop.querySelector('#appConfirmTitle');
            const messageEl = backdrop.querySelector('#appConfirmMessage');
            const cancelBtn = backdrop.querySelector('#appConfirmCancel');
            const acceptBtn = backdrop.querySelector('#appConfirmAccept');

            titleEl.textContent = title || 'Confirmar accion';
            messageEl.textContent = message || 'Esta accion no se puede deshacer.';
            acceptBtn.textContent = confirmText || 'Confirmar';
            acceptBtn.classList.toggle('danger', danger !== false);

            const close = value => {
                backdrop.classList.remove('active');
                document.removeEventListener('keydown', onKeydown);
                cancelBtn.removeEventListener('click', onCancel);
                acceptBtn.removeEventListener('click', onAccept);
                backdrop.removeEventListener('click', onBackdrop);
                resolve(value);
            };

            const onCancel = () => close(false);
            const onAccept = () => close(true);
            const onBackdrop = event => {
                if (event.target === backdrop) close(false);
            };
            const onKeydown = event => {
                if (event.key === 'Escape') close(false);
            };

            cancelBtn.addEventListener('click', onCancel);
            acceptBtn.addEventListener('click', onAccept);
            backdrop.addEventListener('click', onBackdrop);
            document.addEventListener('keydown', onKeydown);

            backdrop.classList.add('active');
            cancelBtn.focus();
        });
    }

    document.querySelectorAll('form[data-confirm]').forEach(form => {
        form.addEventListener('submit', async event => {
            if (form.dataset.confirmed === 'true') return;
            event.preventDefault();
            const ok = await openConfirm({
                title: form.dataset.confirmTitle,
                message: form.dataset.confirm,
                confirmText: form.dataset.confirmText || 'Eliminar',
                danger: form.dataset.confirmDanger !== 'false'
            });
            if (!ok) return;
            form.dataset.confirmed = 'true';
            form.submit();
        });
    });

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
        document.querySelectorAll('.product-card, .product-card-dynamic').forEach(card => {
            const id = card.dataset.id;
            const btnFav = card.querySelector('.btn-fav');
            if (!btnFav) return;
            
            if (favorites.map(String).includes(String(id))) {
                btnFav.textContent = '🖤';
                btnFav.classList.add('active');
            }

            // Remove previous event listeners if any to avoid duplicates
            const newBtnFav = btnFav.cloneNode(true);
            btnFav.parentNode.replaceChild(newBtnFav, btnFav);

            newBtnFav.addEventListener('click', async (e) => {
                e.preventDefault();
                e.stopPropagation();
                if (!id || id === '0') return;
                const isActive = newBtnFav.classList.contains('active');
                if (isActive) {
                    newBtnFav.textContent = '🤍';
                    newBtnFav.classList.remove('active');
                    if (window.showToast) window.showToast('Eliminado de favoritos', '');
                } else {
                    newBtnFav.textContent = '🖤';
                    newBtnFav.classList.add('active');
                    if (window.showToast) window.showToast('¡Añadido a favoritos! 🖤', 'accent');
                }
                try {
                    const formData = new URLSearchParams();
                    formData.append('id', id);
                    const res = await fetch('/api/favorites/toggle', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                        body: formData.toString()
                    });
                    if (res.ok) favorites = await res.json();
                } catch (err) { console.error('Error toggling favorite', err); }
            });
        });
    }

    // === LÓGICA DE CARRITO (BACKEND) ===
    document.querySelectorAll('.product-card, .product-card-dynamic').forEach(card => {
        const btnAdd = card.querySelector('.btn-add-cart');
        if (!btnAdd) return;

        // Clone to remove old listeners
        const newBtnAdd = btnAdd.cloneNode(true);
        btnAdd.parentNode.replaceChild(newBtnAdd, btnAdd);

        newBtnAdd.addEventListener('click', async (e) => {
            e.preventDefault();
            e.stopPropagation();

            const cardId = card.dataset.id;
            const status = card.dataset.status || 'AVAILABLE';
            if (!cardId || cardId === '0') {
                console.warn('Producto sin ID válido, no se puede añadir al carrito');
                return;
            }
            if (status !== 'AVAILABLE') {
                if (window.showToast) window.showToast('Esta pieza no está disponible para compra', '');
                return;
            }

            // --- Selección de talla ---
            const sizesRaw = card.dataset.sizes || '';
            const sizes = sizesRaw ? sizesRaw.split(',').map(s => s.trim()).filter(Boolean) : [];
            let selectedSize = null;

            if (sizes.length > 0) {
                selectedSize = await openSizeModal(sizes, card.dataset.name);
                if (selectedSize === null) return; // usuario canceló
            }

            const product = {
                id: cardId,
                name: card.dataset.name,
                brand: card.dataset.brand,
                price: parseFloat(card.dataset.price.replace(/[$,]/g, '')),
                image: card.dataset.image,
                selectedSize: selectedSize,
                qty: 1
            };

            toggleDrawer(cartDrawer, true);
            await addToCart(product);
            if (window.showToast) window.showToast(`Añadido a tu bolsa 🛑${selectedSize ? ' · ' + selectedSize : ''}`, '');
        });
    });

    async function addToCart(product) {
        // Optimistic update
        const existing = cart.find(item => String(item.id) === String(product.id));
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
                const hasRemoteImage = item.image && (item.image.startsWith('http') || item.image.startsWith('/uploads/'));
                el.innerHTML = `
                    ${hasRemoteImage ? `<img src="${item.image}" alt="${item.name}">` : ''}
                    <div class="cart-item-info">
                        <h4>${item.name}</h4>
                        <p>${item.brand}</p>
                        ${item.selectedSize ? `<p>Talla: ${item.selectedSize}</p>` : ''}
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
