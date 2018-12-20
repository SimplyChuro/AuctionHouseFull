import EmberRouter from '@ember/routing/router';
import config from './config/environment';

const Router = EmberRouter.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function() {
  this.route('error', {path: '/*wildcard'});
  this.route('login');
  this.route('register');
  this.route('home');
  this.route('shop', function() {
    this.route('single-product', { path: 'single-product/:product_id' });
    this.route('about-us');
    this.route('terms-and-conditions');
    this.route('privacy-and-policy');
    this.route('product-list');
  });
  this.route('account', function() {
    this.route('profile');
    this.route('sales', function() {});
    this.route('bids');
    this.route('wishlist');
    this.route('settings');
    this.route('sell', function() {
      this.route('new');
      this.route('entry');
    });

    this.route('admin', function() {
      this.route('users');
      this.route('categories');
      this.route('products');
      this.route('single-user', { path: 'single-user/:user_id' });
      this.route('single-product', { path: 'single-product/:product_id' });
      this.route('single-category', { path: 'single-category/:category_id' });
      this.route('new-category');
      this.route('new-product');
    });
  });
  this.route('password-reset');
  this.route('category-list', { path: 'home/category-list' });
  this.route('register-success');
  this.route('new-password');
  this.route('verify-email');
});

export default Router;
