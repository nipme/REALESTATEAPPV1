import 'dart:math';
import 'package:flutter/material.dart';
import '../models/property.dart';

class PropertyProvider extends ChangeNotifier {
  String _searchQuery = '';
  String _selectedPurpose = 'الكل';
  String _selectedType = 'الكل';
  String _selectedCity = 'الكل';

  final List<Property> _properties = [
    Property(
      id: 1,
      title: "فيلا مودرن فاخرة مع مسبح خاص وحديقة",
      price: 3450000,
      city: "الرياض",
      neighborhood: "حطين",
      purpose: "للبيع",
      type: "فيلا",
      area: 450,
      bedrooms: 5,
      bathrooms: 6,
      description: "فيلا مودرن بتصميم معماري معاصر وتشطيبات سوبر ديلوكس، تكييف مركزي كامل، مصعد بانورامي، ومسبح خاص مع جلسة خارجية فاخرة.",
      imageUrl: "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=800&q=80",
      amenities: ["مسبح خاص", "حديقة واسعة", "غرفة خادمة", "موقف سيارتين", "كاميرات مراقبة", "مصعد خاص"],
      agentName: "سعد بن إبراهيم القحطاني",
      agentPhone: "+966501234567",
      agentLicense: "ترخيص فال: 1200018942",
      isFavorite: true,
    ),
    Property(
      id: 2,
      title: "شقة بإطلالة بانورامية على الواجهة البحرية",
      price: 95000,
      city: "جدة",
      neighborhood: "الشاطئ",
      purpose: "للإيجار",
      type: "شقة",
      area: 185,
      bedrooms: 3,
      bathrooms: 3,
      description: "شقة فاخرة مفروشة بالكامل تطل مباشرة على واجهة جدة البحرية، تمتاز بقربها من الخدمات والمطاعم الراقية، مجهزة بنظام ذكي.",
      imageUrl: "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=800&q=80",
      amenities: ["إطلالة بحرية", "مطبخ راكب", "صالة ألعاب رياضية", "حراسة أمنية 24/7", "دخول ذكي"],
      agentName: "نورة بنت فهد الشهري",
      agentPhone: "+966559876543",
      agentLicense: "ترخيص فال: 1200034511",
      isFavorite: false,
    ),
    Property(
      id: 3,
      title: "دور أرضي مستقل وتشطيب ديلوكس",
      price: 1850000,
      city: "الدمام",
      neighborhood: "الشاطئ الغربي",
      purpose: "للبيع",
      type: "دور",
      area: 320,
      bedrooms: 4,
      bathrooms: 4,
      description: "دور أرضي مستقل بمدخل سيارة وحوش واسع، مجالس ضيافة منفصلة، وموقع هادئ قريب من الواجهة البحرية والمجمعات التجارية.",
      imageUrl: "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800&q=80",
      amenities: ["مدخل مستقل", "حوش واسع", "مجلس رجال مستقل", "مستودع", "غرفة سائق"],
      agentName: "خالد بن عبدالعزيز الشمري",
      agentPhone: "+966541122334",
      agentLicense: "ترخيص فال: 1200059281",
      isFavorite: false,
    ),
    Property(
      id: 4,
      title: "بنتهاوس راقي مع تراس خارجي واسع",
      price: 140000,
      city: "الرياض",
      neighborhood: "الملقا",
      purpose: "للإيجار",
      type: "شقة",
      area: 240,
      bedrooms: 3,
      bathrooms: 4,
      description: "بنتهاوس فاخر في أرقى أحياء الرياض بتراس مفتوح يوفر إطلالة مميزة وخصوصية تامة، تشطيب مودرن وأجهزة مطبخ أوروبية.",
      imageUrl: "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=800&q=80",
      amenities: ["تراس خاص", "مطبخ ألماني", "مواقف قبو", "أمن وحراسة", "تكييف مخفي"],
      agentName: "محمد بن سلطان الدوسري",
      agentPhone: "+966509988776",
      agentLicense: "ترخيص فال: 1200088231",
      isFavorite: false,
    ),
    Property(
      id: 5,
      title: "أرض سكنية في موقع استراتيجي",
      price: 2100000,
      city: "الخبر",
      neighborhood: "الحمراء",
      purpose: "للبيع",
      type: "أرض",
      area: 600,
      bedrooms: 0,
      bathrooms: 0,
      description: "أرض سكنية زاوية على شارعين 20م و 15م، مكتملة الخدمات من كهرباء ومياه وصرف صحي، جاهزة للإفراغ الفوري والبناء.",
      imageUrl: "https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=800&q=80",
      amenities: ["شارعين زاوية", "كهرباء ومياه", "صك إلكتروني", "موقع استثماري"],
      agentName: "فهد بن ناصر السبيعي",
      agentPhone: "+966533445566",
      agentLicense: "ترخيص فال: 1200099412",
      isFavorite: false,
    ),
  ];

  List<Property> get properties {
    return _properties.where((p) {
      final matchesQuery = _searchQuery.isEmpty ||
          p.title.contains(_searchQuery) ||
          p.neighborhood.contains(_searchQuery) ||
          p.city.contains(_searchQuery);
      final matchesPurpose = _selectedPurpose == 'الكل' || p.purpose == _selectedPurpose;
      final matchesType = _selectedType == 'الكل' || p.type == _selectedType;
      final matchesCity = _selectedCity == 'الكل' || p.city == _selectedCity;
      return matchesQuery && matchesPurpose && matchesType && matchesCity;
    }).toList();
  }

  List<Property> get favoriteProperties =>
      _properties.where((p) => p.isFavorite).toList();

  String get searchQuery => _searchQuery;
  String get selectedPurpose => _selectedPurpose;
  String get selectedType => _selectedType;
  String get selectedCity => _selectedCity;

  void setSearchQuery(String query) {
    _searchQuery = query;
    notifyListeners();
  }

  void setPurpose(String purpose) {
    _selectedPurpose = purpose;
    notifyListeners();
  }

  void setType(String type) {
    _selectedType = type;
    notifyListeners();
  }

  void setCity(String city) {
    _selectedCity = city;
    notifyListeners();
  }

  void resetFilters() {
    _searchQuery = '';
    _selectedPurpose = 'الكل';
    _selectedType = 'الكل';
    _selectedCity = 'الكل';
    notifyListeners();
  }

  void toggleFavorite(int id) {
    final index = _properties.indexWhere((p) => p.id == id);
    if (index != -1) {
      _properties[index].isFavorite = !_properties[index].isFavorite;
      notifyListeners();
    }
  }

  void addProperty(Property property) {
    _properties.insert(0, property);
    notifyListeners();
  }

  // حساب أقساط التمويل العقاري الرياضية بدقة
  Map<String, double> calculateMortgage({
    required double price,
    required double downPaymentPercent,
    required int years,
    required double ratePercent,
  }) {
    final downPayment = price * (downPaymentPercent / 100);
    final principal = price - downPayment;
    final monthlyRate = (ratePercent / 100) / 12;
    final totalMonths = years * 12;

    double monthlyInstallment = 0;
    if (monthlyRate > 0) {
      monthlyInstallment = principal *
          (monthlyRate * pow(1 + monthlyRate, totalMonths)) /
          (pow(1 + monthlyRate, totalMonths) - 1);
    } else {
      monthlyInstallment = principal / totalMonths;
    }

    final totalPayment = (monthlyInstallment * totalMonths) + downPayment;
    final totalInterest = totalPayment - price;

    return {
      'monthly': monthlyInstallment,
      'downPayment': downPayment,
      'principal': principal,
      'totalPayment': totalPayment,
      'totalInterest': totalInterest,
    };
  }
}
