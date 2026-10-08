# Fast_Browser_Net

سبک‌وزن‌ترین نسخه مستقل مرورگر PC با WebView سیستم Android.

ویژگی‌ها:
- نمایش Desktop/PC با User-Agent دسکتاپ و viewport مناسب.
- کنترل‌ها فقط در پایین صفحه.
- Back، Forward، Reload، Home و Full Screen.
- دانلود واقعی با Android DownloadManager.
- انتخاب چند فایل و ارسال چندباره/نامحدود از نظر تعداد انتخاب‌های برنامه برای فرم‌های آپلود؛ محدودیت نهایی را سایت، Android و فضای ذخیره‌سازی تعیین می‌کنند.
- انتخاب چندین موتور جستجو: Google، Bing، Yahoo، DuckDuckGo، Brave، Startpage، Ecosia، Qwant، Yandex، Baidu، Mojeek، Ask، AOL و Swisscows.
- تنظیم HTTP/HTTPS Proxy با ProxyController اندروید.
- محل تنظیم DNS و دسترسی به تنظیمات VPN سیستم.
- بدون کتابخانه‌های شخص ثالث و بدون AndroidX برای پایین نگه‌داشتن حجم.
- آیکون اختصاصی Fast Browser Net.

نکته: WebView عمومی Android API مستقیمی برای اعمال DNS سفارشی مستقل از سیستم ندارد؛ بنابراین DNS در تنظیمات نگهداری می‌شود و برای تونل/DNS واقعی باید روش سرویس VPN یا سرور تونل مشخص شود. دکمه VPN تنظیمات واقعی Android را باز می‌کند و قابلیت ساخت VPN را جعل نمی‌کند.

## ST — ارتباط واقعی Proxy/Tunnel
در بخش **Setups → ST** امکان تعریف یک نقطه اتصال واقعی فراهم شده است:
- HTTP/HTTPS Proxy یا SOCKS5 Proxy
- Server / Host و Port
- Connect / Disconnect
- Test connection برای HTTP/HTTPS Proxy
- ذخیره تنظیمات ST
- اعمال Proxy واقعی WebView از طریق Android WebView ProxyController (API 29+)

ST خودش یک سرور تونل ایجاد نمی‌کند؛ برای اتصال واقعی باید Host/Port متعلق به یک Proxy/Tunnel مجاز و قابل دسترس وارد شود. این بخش برای دورزدن هزینه یا محدودیت اپراتور طراحی نشده است.
