import Controller from '@ember/controller';
import { isEmpty } from '@ember/utils';
import { stringSimilarity } from "string-similarity-js";

export default Controller.extend({
  session: Ember.inject.service(),
  store: Ember.inject.service(),

  queryParams: ['name', 'parent_category', 'child_category', 'color', 'size', 'list_type' , 'sorting'],
  name: null,
  parent_category: null,
  child_category: null,
  sorting: null,
  minPrice: null,
  maxPrice: null,
  color: null,
  size: null,
  list_type: null,
  checker: null,
  allWishlistItems: null,
  closestCategory: null,
  nameNotFound: false,

  startRange:[1, 5000],

  wishlist: Ember.computed(function(){
    const store = this.get('store');
    return this.store.findAll('wishlist', { reload: true });
  }).volatile(),

  wishlistItems: Ember.computed('wishlist', function(){
    this.set('allWishlistItems', this.get('wishlist'));

    return this.get('allWishlistItems');
  }).volatile(),

  products: Ember.computed('name', 'parent_category', 'child_category', function(){
    var categoryChecker;

    if(isEmpty(this.get('parent_category'))) {
      categoryChecker = 0;
    } else {
      if(isEmpty(this.get('child_category'))) {
        categoryChecker = this.get('parent_category');
      } else {
        categoryChecker = this.get('child_category');
      }
    }

    var nameChecker = this.get('name');
    if(isEmpty(nameChecker)) {
      nameChecker = '';
    }
    return this.store.query('product', { name: nameChecker, category: categoryChecker, featured: '', status: '', rating: '' });
  }),

  filteredProducts: Ember.computed('name', 'parent_category', 'child_category', 'minPrice', 'maxPrice', 'color', 'size', 'sorting', function(){
 
    var products = this.get('products');
    var _this = this;
    let singleProduct = null;

    if(!(isEmpty(this.get('color')))) {
      products = products.filter(
        function(product) { 
          if(product.color == _this.get('color')) {
            singleProduct = product;
          } else {
            singleProduct = null;
          }
          return singleProduct;
        }
      );
    }

    if(!(isEmpty(this.get('size')))) {
      products = products.filter(
        function(product) { 
          if(product.size == _this.get('size')) {
            singleProduct = product;
          } else {
            singleProduct = null;
          }
          return singleProduct;
        }
      );
    }

    if(!(isEmpty(this.get('minPrice'))) && !(isEmpty(this.get('maxPrice')))) {
      products = products.filter(
        function(product) { 
          console.log(_this.get('maxPrice'));

          if((_this.get('maxPrice') >= product.startingPrice) && (product.startingPrice >= _this.get('minPrice'))) {
            singleProduct = product;
          } else {
            singleProduct = null;
          }
          return singleProduct;
        }
      );
    }

    return products;

  }),

  productSearchErrorMessage: Ember.computed('filteredProducts', function(){

    var categoryChecker = null;
    var similarityRate = 0;
    var _this = this;

    if(isEmpty(this.get('filteredProducts')) && !(isEmpty(this.get('name')))){
      var categories = this.get('model.categoryList');
      _this.set('closestCategory', null);
      categories.forEach((item, index) => {
        if(stringSimilarity(item.name, this.get('name')) > similarityRate){
          similarityRate = stringSimilarity(item.name, this.get('name'));
          _this.set('closestCategory', item);
        }
      });
        _this.set('nameNotFound', true);
      } else {
        _this.set('nameNotFound', false);
      }

      return _this.get('nameNotFound');
  }),

  actions: {
    toggleDetails: function(category) {

      if(category.id !== this.get('parent_category')){
        this.set('parent_category', category.id);
        this.set('child_category', null);
      } else {
        this.set('parent_category', null);
        this.set('child_category', null);
      }

    },

    sliderChanged: function(value) {
      this.set('minPrice', value[0]);
      this.set('maxPrice', value[1]);
    },

    selectCategory: function(selected) {
      if(selected === null){
        this.set('child_category', selected);  
        this.set('parent_category', selected);  
      } else {
        this.set('child_category', selected.id);  
      }
    },

    setColor: function(color) {
      this.set('color', color);
    },

    setSize: function(size) {
      this.set('size', size);  
    },

    setListType: function(type) {
      this.set('list_type', type);  
    },

    setSortType: function(type){
      this.set('sorting', type);  
    },

    createWishlist: function(productID) {
      var _this = this;
      this.store.createRecord('wishlist', {
        status: 'active',
        product_id: productID
      }).save();
    },

    deleteWishlist: function(wishlistItem) {
      var _this = this;
      var currentWishlistItem = wishlistItem;
      currentWishlistItem.destroyRecord().then(function(){
        _this.get('allWishlistItems').removeObject(wishlistItem);
      });
    },

    clearFields: function(){
      this.set('name', undefined);
      this.set('parent_category', undefined);
      this.set('child_category', undefined);
      this.set('sorting', undefined)
      this.set('color', undefined); 
      this.set('size', undefined); 
      this.set('list_type', undefined); 
      this.set('closestCategory', null); 
      this.set('checker', null);
      this.set('allWishlistItems', null);
      this.set('nameNotFound', false);
    }
  }
});
