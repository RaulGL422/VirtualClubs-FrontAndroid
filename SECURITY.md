# Security Policy

## Supported versions

VirtualClubs Android is currently in **Alpha**. Security fixes are applied to the latest version only.

| Version | Supported |
|---------|-----------|
| 0.1.x (latest) | ✅ Yes |
| Older | ❌ No |

---

## Reporting a vulnerability

**Please do not report security vulnerabilities through public GitHub issues.**

If you discover a security vulnerability, send a report by email to:

**raulglprogram@gmail.com**

Include in your report:
- A clear description of the vulnerability
- Steps to reproduce (proof of concept if possible)
- The potential impact (data exposed, attack surface, etc.)
- Any suggested mitigations you have identified

### What to expect

- **Acknowledgement** within 72 hours
- **Status update** within 7 days with an assessment and expected timeline
- **Credit** in the changelog if you wish, once the issue is resolved

We ask that you give us reasonable time to investigate and release a fix before any public disclosure.

---

## Security practices in this project

This project applies the following measures to protect user data:

- **Encrypted token storage** — access and refresh tokens are stored using AES/GCM with AndroidKeyStore. They never touch plain `SharedPreferences`.
- **Certificate pinning** — production builds pin the TLS certificate of the backend API to prevent MITM attacks.
- **No secrets in source code** — all environment-specific values (`GOOGLE_CLIENT_ID`, API URLs) are injected at build time via `BuildConfig` from `secrets.properties`, which is never committed.
- **Local JWT validation** — the `AuthInterceptor` checks token expiration locally before each request to avoid unnecessary network round-trips on expired tokens.
- **Automatic token refresh** — the `SafeResponse` layer handles 401 responses transparently with concurrency protection: only one refresh call is made even when multiple requests fail simultaneously.
