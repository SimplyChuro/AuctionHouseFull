import Controller from '@ember/controller';
import { inject as service } from '@ember/service';
import { isEmpty } from '@ember/utils';
import { stringSimilarity } from "string-similarity-js";
import { computed } from '@ember/object';

export default Controller.extend({
  session: service(),
  store: service(),
  loadingSlider: service(),
  
  queryParams: ['name', 'parent_category', 'child_category', 'color', 'size', 'list_type' , 'sorting'],
  name: null,
  parent_category: null,
  child_category: null,
  sorting: null,
  minPrice: null,
  maxPrice: null,
  staticMaxPrice: 1500,
  color: null,
  size: null,
  list_type: null,
  checker: null,
  allWishlistItems: null,
  closestCategory: null,
  nameNotFound: false,
  listSize: 9,

  startRange:[0, 1500],

  wishlist: computed(function(){
    return this.store.findAll('wishlist', { reload: true });
  }).volatile(),

  wishlistItems: computed('wishlist', function(){
    this.set('allWishlistItems', this.get('wishlist'));
    return this.get('allWishlistItems');
  }).volatile(),

  products: computed('name', 'parent_category', 'child_category', function(){
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

  filteredProducts: computed('products', 'name', 'parent_category', 'child_category', 'minPrice', 'maxPrice', 'color', 'size', 'sorting', 'listSize', function(){
    
    var _this = this;
    let products = this.get('products');

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
          if(_this.get('maxPrice') == 1500) {
            if(product.startingPrice >= _this.get('minPrice')) {
              singleProduct = product;
            } else {
              singleProduct = null;
            }
          } else {
            if((_this.get('maxPrice') >= product.startingPrice) && (product.startingPrice >= _this.get('minPrice'))) {
              singleProduct = product;
            } else {
              singleProduct = null;
            }
          }
          return singleProduct;
        }
      );
    }

    return products;

  }),

  productSearchErrorMessage: computed('filteredProducts', function(){

    var similarityRate = 0.75;
    var _this = this;

    if(isEmpty(this.get('filteredProducts')) && !(isEmpty(this.get('name')))){
      var categories = this.get('model.categoryList');
      _this.set('closestCategory', null);
      categories.forEach((item) => {
        if(stringSimilarity(item.name, this.get('name')) > similarityRate || stringSimilarity(item.name, this.get('name'), 1) > 0.75){
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
        if(this.get('child_category') == selected.id){
          this.set('child_category', null);
        } else {
          this.set('child_category', selected.id); 
        } 
      }
    },

    setColor: function(color) {
      if(this.get('color') == color){
        this.set('color', null);
      } else {
        this.set('color', color);
      }
    },

    setSize: function(size) {
      if(this.get('size') == size){
        this.set('size', null);
      } else {
        this.set('size', size);  
      }
    },

    setListType: function(type) {
      this.set('list_type', type);  
    },

    setSortType: function(type){
      this.set('sorting', type);  
    },

    increaseListSize: function(){
      var currentListSize = this.get('listSize');
      currentListSize += 9;
      this.set('listSize', currentListSize);
    },

    createWishlist: function(productID) {
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
      this.set('minPrice', undefined); 
      this.set('maxPrice', undefined);  
      this.set('list_type', undefined); 
      this.set('closestCategory', null); 
      this.set('checker', null);
      this.set('allWishlistItems', null);
      this.set('nameNotFound', false);
      this.set('listSize', 9);
    }
  }
});
