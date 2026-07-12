(function () {
  const body = document.body;
  const toggle = document.querySelector('[data-sidebar-toggle]');
  const backdrop = document.querySelector('[data-sidebar-backdrop]');

  function openSidebar() {
    body.classList.add('sidebar-open');
  }

  function closeSidebar() {
    body.classList.remove('sidebar-open');
  }

  if (toggle) {
    toggle.addEventListener('click', function () {
      if (body.classList.contains('sidebar-open')) {
        closeSidebar();
      } else {
        openSidebar();
      }
    });
  }

  if (backdrop) {
    backdrop.addEventListener('click', closeSidebar);
  }

  // Close mobile sidebar after nav click
  document.querySelectorAll('.app-nav-link').forEach(function (link) {
    link.addEventListener('click', function () {
      if (window.matchMedia('(max-width: 991.98px)').matches) {
        closeSidebar();
      }
    });
  });
})();
