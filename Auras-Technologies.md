---


---

<h1 id="aura-technologies-–-non‑pci-card-payments-existing-flow">Aura Technologies – Non‑PCI Card Payments (Existing Flow)</h1>
<h2 id="overview">Overview</h2>
<p>Aura Technologies is a non‑PCI‑compliant merchant that uses Flocash’s non‑PCI tokenization flow so that raw card data never touches the merchant backend.</p>
<p>The existing Postman collection <code>Aura Technologies</code> implements the following:</p>
<ul>
<li>Retrieve a public key.</li>
<li>Encrypt card details in the browser.</li>
<li>Create a card token from the encrypted payload.</li>
<li>Create an order using the token.</li>
<li>Handle error scenarios for invalid encryption and invalid currency/country combinations.</li>
</ul>
<hr>
<h2 id="high‑level-flow">High‑Level Flow</h2>
<ol>
<li>Merchant frontend retrieves the Flocash public key.</li>
<li>Customer enters card details on the merchant frontend.</li>
<li>Frontend encrypts card details using the Flocash public key.</li>
<li>Merchant sends encrypted card data to Flocash to obtain a token.</li>
<li>Merchant creates an order using the token.</li>
<li>Flocash processes the payment and returns the transaction result.</li>
</ol>
<hr>
<h2 id="security-model">Security Model</h2>
<ul>
<li>Raw PAN (card number), expiry, and CVV <strong>must never</strong> reach the merchant backend.</li>
<li>Card details are encrypted in the browser using the Flocash public key.</li>
<li>The merchant backend only handles:
<ul>
<li>Encrypted payloads</li>
<li>Tokens</li>
<li>Order metadata</li>
</ul>
</li>
<li>CVV must never be stored.</li>
<li>Public keys may rotate periodically.</li>
<li>Tokenized transactions may still require issuer authentication (e.g., 3DS).</li>
</ul>
<hr>
<h2 id="endpoints-as-implemented-in-the-collection">Endpoints (As Implemented in the Collection)</h2>
<h3 id="retrieve-public-key">1. Retrieve Public Key</h3>
<p><strong>Request</strong></p>
<pre class=" language-http"><code class="prism  language-http">GET {{baseUrl}}/rest/card/token/generate
</code></pre>
<p><strong>Response</strong></p>
<pre class=" language-http"><code class="prism  language-http">200 OK
<span class="token header-name keyword">Content-Type:</span> application/json
</code></pre>
<pre class=" language-json"><code class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"publicKey"</span><span class="token punctuation">:</span> <span class="token string">"2048#10001#9b28f950d6e426f53081fd1d32a0ec42c811158d1c39d0281bed2d45807853dc7ec9cc338f496897f16ce933a45672c9eef364b3f669a4539fd1dc22cf5b981b43ec004bca6326f80eb58cde79a888763683bd7ed18832a49b8f1fc7c3ca24475c9f05af5c37b9c849c0110337892f245e356fdb678b7a7c28acb0b166d689a2f28c2449d4b342bc0ea9537bc3bddf7cc271fef3dd09237d5b2f9c60cd036594b696219e5e9c51f11c0be93783d993b0ec9eaf3e6fb6501435b544fd19fdb8df38f9cdbc20474dcb825482f453b6617e0f6bc4ee54c1a073d41c8384eab2991fba126201b0c5ac5b451b77e0f18eb963e0c97a847875b90c177cb76214f1897b"</span>
<span class="token punctuation">}</span>
</code></pre>
<hr>
<h3 id="verify-public-key">2. Verify Public Key</h3>
<p><strong>Request</strong></p>
<pre class=" language-http"><code class="prism  language-http">GET {{baseUrl}}/rest/card/token/key
</code></pre>
<p><strong>Response</strong></p>
<pre class=" language-http"><code class="prism  language-http">200 OK
<span class="token header-name keyword">Content-Type:</span> application/json
</code></pre>
<pre class=" language-json"><code class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"publicKey"</span><span class="token punctuation">:</span> <span class="token string">"2048#10001#9b28f950d6e426f53081fd1d32a0ec42c811158d1c39d0281bed2d45807853dc7ec9cc338f496897f16ce933a45672c9eef364b3f669a4539fd1dc22cf5b981b43ec004bca6326f80eb58cde79a888763683bd7ed18832a49b8f1fc7c3ca24475c9f05af5c37b9c849c0110337892f245e356fdb678b7a7c28acb0b166d689a2f28c2449d4b342bc0ea9537bc3bddf7cc271fef3dd09237d5b2f9c60cd036594b696219e5e9c51f11c0be93783d993b0ec9eaf3e6fb6501435b544fd19fdb8df38f9cdbc20474dcb825482f453b6617e0f6bc4ee54c1a073d41c8384eab2991fba126201b0c5ac5b451b77e0f18eb963e0c97a847875b90c177cb76214f1897b"</span>
<span class="token punctuation">}</span>
</code></pre>
<hr>
<h3 id="create-card-token-non‑pci">3. Create Card Token (Non‑PCI)</h3>
<p><strong>Request</strong></p>
<pre class=" language-http"><code class="prism  language-http">POST {{baseUrl}}/rest/card/token
<span class="token header-name keyword">Content-Type:</span> application/json
</code></pre>
<pre class=" language-json"><code class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"cardInfo"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"algorithm"</span><span class="token punctuation">:</span> <span class="token string">"RSA1_5"</span><span class="token punctuation">,</span>
    <span class="token string">"encrypt"</span><span class="token punctuation">:</span> <span class="token string">"{{generatedEncrypt}}"</span>
  <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre>
<p><strong>Response</strong></p>
<pre class=" language-http"><code class="prism  language-http">201 Created
<span class="token header-name keyword">Content-Type:</span> application/json
</code></pre>
<pre class=" language-json"><code class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"cardInfo"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"token"</span><span class="token punctuation">:</span> <span class="token string">"358iab4fome0qdq5opl0s1d6s"</span><span class="token punctuation">,</span>
    <span class="token string">"expiredDate"</span><span class="token punctuation">:</span> <span class="token number">2182498249743</span><span class="token punctuation">,</span>
    <span class="token string">"mask"</span><span class="token punctuation">:</span> <span class="token string">"5123**0008"</span><span class="token punctuation">,</span>
    <span class="token string">"storeCard"</span><span class="token punctuation">:</span> <span class="token boolean">false</span>
  <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre>
<p>The merchant stores <code>token</code> for future payments.</p>
<hr>
<h3 id="create-order-using-token">4. Create Order Using Token</h3>
<p><strong>Request</strong></p>
<pre class=" language-http"><code class="prism  language-http">POST {{baseUrl}}/rest/v2/orders
<span class="token header-name keyword">Content-Type:</span> application/json
</code></pre>
<pre class=" language-json"><code class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"order"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"amount"</span><span class="token punctuation">:</span> <span class="token number">9500</span><span class="token punctuation">,</span>
    <span class="token string">"orderId"</span><span class="token punctuation">:</span> <span class="token string">"14052026"</span><span class="token punctuation">,</span>
    <span class="token string">"item_name"</span><span class="token punctuation">:</span> <span class="token string">"Lenovo Legion"</span><span class="token punctuation">,</span>
    <span class="token string">"quantity"</span><span class="token punctuation">:</span> <span class="token number">1</span><span class="token punctuation">,</span>
    <span class="token string">"currency"</span><span class="token punctuation">:</span> <span class="token string">"KES"</span><span class="token punctuation">,</span>
    <span class="token string">"threeDS"</span><span class="token punctuation">:</span> <span class="token boolean">false</span>
  <span class="token punctuation">}</span><span class="token punctuation">,</span>
  <span class="token string">"merchant"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"merchantAccount"</span><span class="token punctuation">:</span> <span class="token string">"{{merchantAccount}}"</span>
  <span class="token punctuation">}</span><span class="token punctuation">,</span>
  <span class="token string">"payer"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"country"</span><span class="token punctuation">:</span> <span class="token string">"KE"</span><span class="token punctuation">,</span>
    <span class="token string">"firstName"</span><span class="token punctuation">:</span> <span class="token string">"Shabiha"</span><span class="token punctuation">,</span>
    <span class="token string">"lastName"</span><span class="token punctuation">:</span> <span class="token string">"Denis"</span><span class="token punctuation">,</span>
    <span class="token string">"mobile"</span><span class="token punctuation">:</span> <span class="token string">"+254720026929"</span><span class="token punctuation">,</span>
    <span class="token string">"email"</span><span class="token punctuation">:</span> <span class="token string">"dsha@gmail.com"</span>
  <span class="token punctuation">}</span><span class="token punctuation">,</span>
  <span class="token string">"payOption"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"id"</span><span class="token punctuation">:</span> <span class="token number">145</span>
  <span class="token punctuation">}</span><span class="token punctuation">,</span>
  <span class="token string">"cardInfo"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"cvv"</span><span class="token punctuation">:</span> <span class="token string">"100"</span><span class="token punctuation">,</span>
    <span class="token string">"algorithm"</span><span class="token punctuation">:</span> <span class="token string">"TOKEN"</span><span class="token punctuation">,</span>
    <span class="token string">"token"</span><span class="token punctuation">:</span> <span class="token string">"{{non-pci-card-token}}"</span>
  <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre>
<p><strong>Response (Success)</strong></p>
<pre class=" language-http"><code class="prism  language-http">201 Created
<span class="token header-name keyword">Content-Type:</span> application/json
</code></pre>
<pre class=" language-json"><code class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"order"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"amount"</span><span class="token punctuation">:</span> <span class="token number">9500</span><span class="token punctuation">,</span>
    <span class="token string">"capturedAmount"</span><span class="token punctuation">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
    <span class="token string">"refundedAmount"</span><span class="token punctuation">:</span> <span class="token number">0</span><span class="token punctuation">,</span>
    <span class="token string">"orderDate"</span><span class="token punctuation">:</span> <span class="token number">1786783353000</span><span class="token punctuation">,</span>
    <span class="token string">"currency"</span><span class="token punctuation">:</span> <span class="token string">"404"</span><span class="token punctuation">,</span>
    <span class="token string">"currencyName"</span><span class="token punctuation">:</span> <span class="token string">"KES"</span><span class="token punctuation">,</span>
    <span class="token string">"custom"</span><span class="token punctuation">:</span> <span class="token string">"34850196"</span><span class="token punctuation">,</span>
    <span class="token string">"item_name"</span><span class="token punctuation">:</span> <span class="token string">"Lenovo Legion"</span><span class="token punctuation">,</span>
    <span class="token string">"quantity"</span><span class="token punctuation">:</span> <span class="token string">"1"</span><span class="token punctuation">,</span>
    <span class="token string">"orderId"</span><span class="token punctuation">:</span> <span class="token string">"14052026"</span><span class="token punctuation">,</span>
    <span class="token string">"tracking"</span><span class="token punctuation">:</span> <span class="token string">"dd8e3d83-5268-4795-ba03-b0519966e555"</span><span class="token punctuation">,</span>
    <span class="token string">"traceNumber"</span><span class="token punctuation">:</span> <span class="token string">"T68896853005808"</span><span class="token punctuation">,</span>
    <span class="token string">"partnerMessage"</span><span class="token punctuation">:</span> <span class="token string">"AUTHORIZED:0815MCC849113"</span><span class="token punctuation">,</span>
    <span class="token string">"status"</span><span class="token punctuation">:</span> <span class="token string">"0012"</span><span class="token punctuation">,</span>
    <span class="token string">"statusDesc"</span><span class="token punctuation">:</span> <span class="token string">"Authorized"</span><span class="token punctuation">,</span>
    <span class="token string">"partnerTxn"</span><span class="token punctuation">:</span> <span class="token string">"7867833564096849403812"</span><span class="token punctuation">,</span>
    <span class="token string">"paymentChannel"</span><span class="token punctuation">:</span> <span class="token string">"Card"</span><span class="token punctuation">,</span>
    <span class="token string">"payer"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
      <span class="token string">"firstName"</span><span class="token punctuation">:</span> <span class="token string">"Shabiha"</span><span class="token punctuation">,</span>
      <span class="token string">"lastName"</span><span class="token punctuation">:</span> <span class="token string">"Denis"</span><span class="token punctuation">,</span>
      <span class="token string">"email"</span><span class="token punctuation">:</span> <span class="token string">"dsha@gmail.com"</span><span class="token punctuation">,</span>
      <span class="token string">"mobile"</span><span class="token punctuation">:</span> <span class="token string">"+254720026929"</span>
    <span class="token punctuation">}</span><span class="token punctuation">,</span>
    <span class="token string">"payOption"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
      <span class="token string">"id"</span><span class="token punctuation">:</span> <span class="token number">145</span><span class="token punctuation">,</span>
      <span class="token string">"displayName"</span><span class="token punctuation">:</span> <span class="token string">"Debit / Credit Cards"</span><span class="token punctuation">,</span>
      <span class="token string">"type"</span><span class="token punctuation">:</span> <span class="token string">"CARD"</span><span class="token punctuation">,</span>
      <span class="token string">"logo"</span><span class="token punctuation">:</span> <span class="token string">"https://sandbox.flocash.com/images/logos/50/visamaster-new.png"</span>
    <span class="token punctuation">}</span><span class="token punctuation">,</span>
    <span class="token string">"approveCode"</span><span class="token punctuation">:</span> <span class="token string">"751877"</span><span class="token punctuation">,</span>
    <span class="token string">"cardInfo"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
      <span class="token string">"token"</span><span class="token punctuation">:</span> <span class="token string">"358iab4fome0qdq5opl0s1d6s"</span><span class="token punctuation">,</span>
      <span class="token string">"storeCard"</span><span class="token punctuation">:</span> <span class="token boolean">false</span>
    <span class="token punctuation">}</span><span class="token punctuation">,</span>
    <span class="token string">"redirect"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
      <span class="token string">"params"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span><span class="token punctuation">}</span>
    <span class="token punctuation">}</span>
  <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre>
<hr>
<h2 id="error-scenarios-as-implemented">Error Scenarios (As Implemented)</h2>
<h3 id="invalid-encryption-payload">1. Invalid Encryption Payload</h3>
<p><strong>Request</strong></p>
<pre class=" language-http"><code class="prism  language-http">POST {{baseUrl}}/rest/card/token
<span class="token header-name keyword">Content-Type:</span> application/json
</code></pre>
<pre class=" language-json"><code class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"cardInfo"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"algorithm"</span><span class="token punctuation">:</span> <span class="token string">"RSA1_5"</span><span class="token punctuation">,</span>
    <span class="token string">"encrypt"</span><span class="token punctuation">:</span> <span class="token string">"eyJhbGciOiJSU0ExXzUiLCJlbmMiOiJBMjU2R0NNIiwia2lkIjoiMjA0OCIsImNvbS5mbG9jYXNoLmFwaVZlcnNpb24iOiIxLjAiLCJjb20uZmxvY2FzaC5saWJWZXJzaW9uIjoiMS4wLjAiLCJjb20uZmxvY2FzaC5jaGFubmVsIjoiamF2YXNjcmlwdCJ9.exT6X2ZmEs1yaiJUUq+H5eWI0Uu7fjxRwfU7Py61RNVHrRD3o7jxrLmrAcj0msMFiFIvz+HX7T7i03q5ptl0NhzZgAGw4asUEd/WKwwBQIZ23ki6T0CgEE1Vjk2oyFbqSGX1Ex2f6aX5gYCQvcjOatCeVIApoi5Ph5eFNIzgKEwnp2vKH4saQkOkow7pAlprbR74O4WTHhaPTvtho9EUD+rJ5nVFfo4pAoXCdhJX0NvUK6MM3k3Oe53STmmzq+ifH3WjV3XVQCMBc4pm39H8PieXGMX5XxVgHkjAIrvqryLFUqWWlfXqDIquN+tIHR3NRhoeXdNYSETnma5emf1bMQ==.ussssGcfJzzyEktGajE6.3fianXGoef7ArmCqXp4IAVFCDKDwUN7GCBah4+VHFy0dAODk7eWNJTda0P8JNFwWRwO766P73a4CUj0pJdtO1l2WW8FL4MD2CeuczNRXtaTuCVa7GgwL49/DtTg16bQ75mDLZEjRX00IDgF8QQem+Q49p/nNa89A+ml4zyi/84A=.2bhO++ygPdAhFZVDOwFbg=="</span>
  <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre>
<p><strong>Response</strong></p>
<pre class=" language-http"><code class="prism  language-http">400 Bad Request
<span class="token header-name keyword">Error-Id:</span> 0817
<span class="token header-name keyword">Error-Code:</span> CARD_TOKEN_CANT_PROCESS_RSA
<span class="token header-name keyword">Error-Message:</span> Can not process RSA
</code></pre>
<hr>
<h3 id="invalid-currency--country-combination">2. Invalid Currency / Country Combination</h3>
<p><strong>Request</strong></p>
<pre class=" language-http"><code class="prism  language-http">POST {{baseUrl}}/rest/v2/orders
<span class="token header-name keyword">Content-Type:</span> application/json
</code></pre>
<pre class=" language-json"><code class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"order"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"amount"</span><span class="token punctuation">:</span> <span class="token number">9500</span><span class="token punctuation">,</span>
    <span class="token string">"orderId"</span><span class="token punctuation">:</span> <span class="token string">"14052026"</span><span class="token punctuation">,</span>
    <span class="token string">"item_name"</span><span class="token punctuation">:</span> <span class="token string">"Lenovo Legion"</span><span class="token punctuation">,</span>
    <span class="token string">"quantity"</span><span class="token punctuation">:</span> <span class="token number">1</span><span class="token punctuation">,</span>
    <span class="token string">"currency"</span><span class="token punctuation">:</span> <span class="token string">"US"</span><span class="token punctuation">,</span>
    <span class="token string">"threeDS"</span><span class="token punctuation">:</span> <span class="token boolean">false</span>
  <span class="token punctuation">}</span><span class="token punctuation">,</span>
  <span class="token string">"merchant"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"merchantAccount"</span><span class="token punctuation">:</span> <span class="token string">"{{merchantAccount}}"</span>
  <span class="token punctuation">}</span><span class="token punctuation">,</span>
  <span class="token string">"payer"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"country"</span><span class="token punctuation">:</span> <span class="token string">"TZ"</span><span class="token punctuation">,</span>
    <span class="token string">"firstName"</span><span class="token punctuation">:</span> <span class="token string">"Shabiha"</span><span class="token punctuation">,</span>
    <span class="token string">"lastName"</span><span class="token punctuation">:</span> <span class="token string">"Denis"</span><span class="token punctuation">,</span>
    <span class="token string">"mobile"</span><span class="token punctuation">:</span> <span class="token string">"+254720026929"</span><span class="token punctuation">,</span>
    <span class="token string">"email"</span><span class="token punctuation">:</span> <span class="token string">"dsha@gmail.com"</span>
  <span class="token punctuation">}</span><span class="token punctuation">,</span>
  <span class="token string">"payOption"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"id"</span><span class="token punctuation">:</span> <span class="token number">145</span>
  <span class="token punctuation">}</span><span class="token punctuation">,</span>
  <span class="token string">"cardInfo"</span><span class="token punctuation">:</span> <span class="token punctuation">{</span>
    <span class="token string">"cvv"</span><span class="token punctuation">:</span> <span class="token string">"100"</span><span class="token punctuation">,</span>
    <span class="token string">"algorithm"</span><span class="token punctuation">:</span> <span class="token string">"TOKEN"</span><span class="token punctuation">,</span>
    <span class="token string">"token"</span><span class="token punctuation">:</span> <span class="token string">"{{non-pci-card-token}}"</span>
  <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre>
<p><strong>Response</strong></p>
<pre class=" language-http"><code class="prism  language-http">400 Bad Request
<span class="token header-name keyword">Error-Id:</span> 0110
<span class="token header-name keyword">Error-Code:</span> WRONG_CURRENCY
<span class="token header-name keyword">Error-Message:</span> Invalid currency.
</code></pre>
<hr>
<h2 id="field-summary-from-the-existing-json">Field Summary (From the Existing JSON)</h2>
<h3 id="order-request-fields">Order Request Fields</h3>

<table>
<thead>
<tr>
<th>Field</th>
<th>Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td><code>order</code></td>
<td>object</td>
<td>Order details (amount, currency, etc.).</td>
</tr>
<tr>
<td><code>merchant</code></td>
<td>object</td>
<td>Merchant account information.</td>
</tr>
<tr>
<td><code>payer</code></td>
<td>object</td>
<td>Payer details (name, contact, country).</td>
</tr>
<tr>
<td><code>payOption</code></td>
<td>object</td>
<td>Payment option (e.g., card).</td>
</tr>
<tr>
<td><code>payOption.id</code></td>
<td>number</td>
<td>Identifier for the payment option (e.g., <code>145</code>).</td>
</tr>
<tr>
<td><code>cardInfo</code></td>
<td>object</td>
<td>Card-related information.</td>
</tr>
<tr>
<td><code>cardInfo.token</code></td>
<td>string</td>
<td>Previously generated card token.</td>
</tr>
<tr>
<td><code>cardInfo.cvv</code></td>
<td>string</td>
<td>CVV for the transaction.</td>
</tr>
<tr>
<td><code>cardInfo.algorithm</code></td>
<td>string</td>
<td>Algorithm type (<code>TOKEN</code>).</td>
</tr>
</tbody>
</table><p>No explicit card-network selection field (such as <code>cardNetwork</code>) exists in this collection.</p>
<hr>
<h2 id="integration-notes-based-on-existing-json-only">Integration Notes (Based on Existing JSON Only)</h2>
<ul>
<li>
<p>The frontend must:</p>
<ul>
<li>Retrieve the public key from <code>/rest/card/token/generate</code>.</li>
<li>Encrypt card details in the browser.</li>
<li>Send the encrypted payload to <code>/rest/card/token</code> to obtain a token.</li>
<li>Use that token in the order request to <code>/rest/v2/orders</code>.</li>
</ul>
</li>
<li>
<p>The backend must:</p>
<ul>
<li>Validate the encrypted payload and token.</li>
<li>Validate currency and country combinations.</li>
<li>Return appropriate error codes and messages for invalid requests.</li>
</ul>
</li>
<li>
<p>The collection does <strong>not</strong>:</p>
<ul>
<li>Include any explicit card-network selection (Visa/Mastercard).</li>
<li>Include any MID selection or routing fields.</li>
<li>Change behavior based on card brand; routing is determined by existing platform configuration, not by a field in this JSON.</li>
</ul>
</li>
</ul>
<hr>
<h2 id="test-scenarios-from-the-existing-collection">Test Scenarios (From the Existing Collection)</h2>
<h3 id="positive-tests">Positive Tests</h3>
<ul>
<li><code>NON-PCI-001</code>: Generate key → <code>200</code> with <code>publicKey</code>.</li>
<li><code>NON-PCI-002</code>: Get key → <code>200</code> and verify <code>publicKey</code> matches stored value.</li>
<li><code>NON-PCI-003</code>: Create token → <code>201</code> with <code>cardInfo.token</code>.</li>
<li><code>NON-PCI-004</code>: Create order with token → <code>201</code> with <code>order.status = "0012"</code> and <code>statusDesc = "Authorized"</code>.</li>
</ul>
<h3 id="negative-tests">Negative Tests</h3>
<ul>
<li>
<p><code>NON-PCI-005</code>: Create token with invalid encrypt → <code>400</code> with:</p>
<ul>
<li><code>Error-Code: CARD_TOKEN_CANT_PROCESS_RSA</code></li>
<li><code>Error-Message: Can not process RSA</code></li>
</ul>
</li>
<li>
<p><code>NON-PCI-006</code>: Create order with invalid currency/country → <code>400</code> with:</p>
<ul>
<li><code>Error-Code: WRONG_CURRENCY</code></li>
<li><code>Error-Message: Invalid currency.</code></li>
</ul>
</li>
</ul>
<hr>
<h2 id="summary">Summary</h2>
<p>This document describes <strong>only</strong> the non‑PCI card payment flow as implemented in the existing <code>Aura Technologies</code> Postman collection JSON:</p>
<ul>
<li>Public key retrieval.</li>
<li>Client‑side encryption.</li>
<li>Token creation.</li>
<li>Order creation using the token.</li>
<li>Error handling for invalid encryption and invalid currency/country.</li>
</ul>
<p>No card-network selection field (such as <code>cardNetwork</code>) is present or used in this JSON. All routing and network handling is based on existing platform configuration, not on any explicit network field in the request.</p>

