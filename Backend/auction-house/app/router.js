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
  this.route('home', function() {});
  this.route('shop', function() {
    this.route('single-product', { path: 'single-product/:product_id' });
    this.route('about-us');
    this.route('terms-and-conditions');
    this.route('privacy-and-policy');
    this.route('product-list');
  });
  this.route('account', function() {
    this.route('profile');
    this.route('sales', function() {
      this.route('active');
      this.route('sold');
    });
    this.route('bids');
    this.route('wishlist');
    this.route('settings');
    this.route('sell');
  });
  this.route('password-reset');
  this.route('category-list', { path: 'home/category-list' });
  this.route('register-success');
});

export default Router;
