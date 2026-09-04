class Property {
  final int id;
  final String title;
  final double price;
  final String currency;
  final String city;
  final String neighborhood;
  final String purpose; // "للبيع" أو "للإيجار"
  final String type; // "فيلا"، "شقة"، "أرض"، "دور"
  final double area;
  final int bedrooms;
  final int bathrooms;
  final String description;
  final String imageUrl;
  final List<String> amenities;
  final String agentName;
  final String agentPhone;
  final String agentLicense;
  bool isFavorite;

  Property({
    required this.id,
    required this.title,
    required this.price,
    this.currency = "ر.س",
    required this.city,
    required this.neighborhood,
    required this.purpose,
    required this.type,
    required this.area,
    required this.bedrooms,
    required this.bathrooms,
    required this.description,
    required this.imageUrl,
    required this.amenities,
    required this.agentName,
    required this.agentPhone,
    required this.agentLicense,
    this.isFavorite = false,
  });

  Property copyWith({
    int? id,
    String? title,
    double? price,
    String? currency,
    String? city,
    String? neighborhood,
    String? purpose,
    String? type,
    double? area,
    int? bedrooms,
    int? bathrooms,
    String? description,
    String? imageUrl,
    List<String>? amenities,
    String? agentName,
    String? agentPhone,
    String? agentLicense,
    bool? isFavorite,
  }) {
    return Property(
      id: id ?? this.id,
      title: title ?? this.title,
      price: price ?? this.price,
      currency: currency ?? this.currency,
      city: city ?? this.city,
      neighborhood: neighborhood ?? this.neighborhood,
      purpose: purpose ?? this.purpose,
      type: type ?? this.type,
      area: area ?? this.area,
      bedrooms: bedrooms ?? this.bedrooms,
      bathrooms: bathrooms ?? this.bathrooms,
      description: description ?? this.description,
      imageUrl: imageUrl ?? this.imageUrl,
      amenities: amenities ?? this.amenities,
      agentName: agentName ?? this.agentName,
      agentPhone: agentPhone ?? this.agentPhone,
      agentLicense: agentLicense ?? this.agentLicense,
      isFavorite: isFavorite ?? this.isFavorite,
    );
  }
}
