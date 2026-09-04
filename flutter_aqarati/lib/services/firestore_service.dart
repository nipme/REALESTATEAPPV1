import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_core/firebase_core.dart';
import '../models/property.dart';

class FirestoreService {
  static final FirestoreService _instance = FirestoreService._internal();
  factory FirestoreService() => _instance;
  FirestoreService._internal();

  FirebaseFirestore? _db;

  bool get isInitialized => _db != null;

  void init() {
    try {
      if (Firebase.apps.isNotEmpty) {
        _db = FirebaseFirestore.instance;
      }
    } catch (_) {}
  }

  final String propertiesCollection = 'properties';
  final String favoritesCollection = 'favorites';

  Future<void> saveProperty(Property property) async {
    try {
      init();
      if (_db == null) return;
      await _db!.collection(propertiesCollection).doc(property.id.toString()).set({
        'id': property.id,
        'title': property.title,
        'price': property.price,
        'currency': property.currency,
        'city': property.city,
        'neighborhood': property.neighborhood,
        'purpose': property.purpose,
        'type': property.type,
        'area': property.area,
        'bedrooms': property.bedrooms,
        'bathrooms': property.bathrooms,
        'description': property.description,
        'imageUrl': property.imageUrl,
        'amenities': property.amenities,
        'agentName': property.agentName,
        'agentPhone': property.agentPhone,
        'agentLicense': property.agentLicense,
        'isFavorite': property.isFavorite,
        'updatedAt': FieldValue.serverTimestamp(),
      }, SetOptions(merge: true));
    } catch (_) {}
  }

  Future<void> updateFavorite(int propertyId, bool isFavorite) async {
    try {
      init();
      if (_db == null) return;
      await _db!.collection(favoritesCollection).doc(propertyId.toString()).set({
        'propertyId': propertyId,
        'isFavorite': isFavorite,
        'updatedAt': FieldValue.serverTimestamp(),
      }, SetOptions(merge: true));

      await _db!.collection(propertiesCollection).doc(propertyId.toString()).set({
        'isFavorite': isFavorite,
      }, SetOptions(merge: true));
    } catch (_) {}
  }

  Future<List<Property>> fetchProperties() async {
    try {
      init();
      if (_db == null) return [];
      final snapshot = await _db!.collection(propertiesCollection).get();
      return snapshot.docs.map((doc) {
        final d = doc.data();
        return Property(
          id: (d['id'] as num?)?.toInt() ?? 0,
          title: d['title'] ?? '',
          price: (d['price'] as num?)?.toDouble() ?? 0.0,
          currency: d['currency'] ?? 'ر.س',
          city: d['city'] ?? '',
          neighborhood: d['neighborhood'] ?? '',
          purpose: d['purpose'] ?? 'للبيع',
          type: d['type'] ?? 'شقة',
          area: (d['area'] as num?)?.toDouble() ?? 0.0,
          bedrooms: (d['bedrooms'] as num?)?.toInt() ?? 0,
          bathrooms: (d['bathrooms'] as num?)?.toInt() ?? 0,
          description: d['description'] ?? '',
          imageUrl: d['imageUrl'] ?? '',
          amenities: (d['amenities'] as List<dynamic>?)?.map((e) => e.toString()).toList() ?? [],
          agentName: d['agentName'] ?? '',
          agentPhone: d['agentPhone'] ?? '',
          agentLicense: d['agentLicense'] ?? '',
          isFavorite: d['isFavorite'] ?? false,
        );
      }).toList();
    } catch (_) {
      return [];
    }
  }
}
